package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.awt.Dimension;
import java.util.Properties;

import org.omg.CORBA.ORB;

/**
 * 携帯電話アプリの制御管理を行うクラス。
 * 
 * @author Acroquest
 * 
 */
public class PhoneController {
	/** ポート番号で不正な値を入力された時に返す値。 */
	private static final int INVALID_PORT_NUMBER = -1;

	/** ポート番号として設定できる値の最小値。 */
	private static final int MINMUM_PORT_NUMBER = 900;

	/** ポート番号として設定できる値の最大値。 */
	private static final int MAXMUM_PORT_NUMBER = 65535;

	/** 初期化処理が行われたか判断するフラグ。 */
	private boolean isInitialized_;

	/** ORB. */
	private org.omg.CORBA.ORB orb_;

	/** 携帯電話からの接続を受信するサーバ。 */
	private PhoneServer phoneServer_;

	/** 現在通信中の携帯電話の名前。 */
	private String otherPhoneName_;

	/** 携帯電話ウィンドウ。 */
	private PhoneFrame phoneFrame_;

	/**
	 * コンストラクタ。 コンストラクタ実行後、初期化メソッドinitialize()を実行する必要がある。
	 * 
	 */
	public PhoneController() {
		this.phoneFrame_ = new PhoneFrame(this);
		this.isInitialized_ = false;
	}

	/**
	 * 初期化処理。
	 * 
	 * @param frameSize
	 *            画面サイズ
	 */
	public void initialize(Dimension frameSize) {
		if (this.isInitialized_ == true) {
			return;
		}

		this.isInitialized_ = true;
		PhoneWindowListener windowListener = new PhoneWindowListener(this);
		this.phoneFrame_.addWindowListener(windowListener);
		this.phoneFrame_.setSize(frameSize);
		this.phoneFrame_.setVisible(true);
	}

	/**
	 * 電源をオンにし、接続受信処理を開始する。
	 * 
	 */
	public void startPhoneServer() {
		String ownName = this.phoneFrame_.getOwnName();

		if ((ownName == null) || ("".equals(ownName.trim()) == true)) {
			String errMsg = "自分の名前を入力して下さい.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);

			return;
		}

		String namingServiceHost = this.phoneFrame_.getNamingServiceHost();
		if (this.checkName(namingServiceHost) == false) {
			String errMsg = "ネーミングサービスのホストを入力してください.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);

			return;
		}

		String namingServicePortStr = this.phoneFrame_
				.getNamingServicePortStr();
		int namingServicePortNumber = this
				.getPortNumberFromStr(namingServicePortStr);

		if (namingServicePortNumber == INVALID_PORT_NUMBER) {
			String errMsg = "ポート番号は " + MINMUM_PORT_NUMBER + " ～ "
					+ MAXMUM_PORT_NUMBER + " の整数値を指定してください.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);

			return;
		}

		this.orb_ = this.initOrb(namingServiceHost, namingServicePortStr);

		this.phoneServer_ = new PhoneServer(this.orb_);
		HandyPhoneImpl handyPhoneImpl = new HandyPhoneImpl(this);

		try {
			this.phoneServer_.startServer(ownName, handyPhoneImpl);
		} catch (CorbaOperationException coExc) {
			String errMsg = "ORBへの接続に失敗しました.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			this.phoneServer_ = null;

			coExc.printStackTrace();
			return;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
		this.phoneFrame_.appendText("電源をオンにしました.");
	}

	/**
	 * 電源をオフにする。
	 * 
	 */
	public synchronized void terminateServer() {
		// サーバが起動していない場合
		if (this.phoneServer_ == null) {
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			return;
		}

		String ownName = this.phoneFrame_.getOwnName();
		try {
			this.phoneServer_.terminateServer(ownName);
		} catch (CorbaOperationException coExc) {
			String errMsg = "電源オフ処理に失敗しました.";
			this.phoneFrame_.appendText(errMsg);
			return;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
	}

	/**
	 * 接続要求を受信した時に呼び出される。
	 * 
	 * @param senderName
	 *            　送信者名
	 * @param recieverName
	 *            受信者名
	 */
	public synchronized boolean acceptConnection(String senderName,
			String recieverName) {
		boolean isValidRecieverName = this.checkName(recieverName);
		if (isValidRecieverName == false) {
			return false;
		}

		String ownName = this.phoneFrame_.getOwnName();
		if (recieverName.equals(ownName.trim()) == false) {
			return false;
		}

		// 既に通信中の場合、接続を拒否する
		if ((this.otherPhoneName_ != null)
				&& ("".equals(this.otherPhoneName_.trim()) == false)) {
			return false;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);
		this.phoneFrame_.setConnectName(senderName);

		String message = senderName + " からの接続を受信しました.";
		this.phoneFrame_.appendText(message);
		this.otherPhoneName_ = senderName;

		return true;
	}

	/**
	 * 接続遮断要求を受信したときに呼び出される。
	 * 
	 * @param senderName
	 *            送信者名
	 * @param recieverName
	 *            受信者名
	 * @return
	 */
	public synchronized boolean acceptDisConnection(String senderName,
			String recieverName) {
		boolean isValidSenderName = this.checkName(senderName);
		boolean isValidRecieverName = this.checkName(recieverName);
		if ((isValidSenderName == false) || (isValidRecieverName == false)) {
			return false;
		}

		String ownName = this.phoneFrame_.getOwnName();
		if (recieverName.equals(ownName.trim()) == false) {
			return false;
		}

		if ((this.otherPhoneName_ == null)
				|| (senderName.equals(this.otherPhoneName_.trim()) == false)) {
			return false;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);

		String message = senderName + " から接続が遮断されました.";
		this.phoneFrame_.appendText(message);
		this.otherPhoneName_ = null;

		return true;
	}

	/**
	 * 他携帯電話アプリに接続する。
	 * 
	 */
	public synchronized void startConnection() {
		String ownName = this.phoneFrame_.getOwnName();
		boolean isValidOwnName = this.checkName(ownName);

		String otherName = this.phoneFrame_.getConnectName();
		boolean isValidOtherName = this.checkName(otherName);

		if ((isValidOwnName == false) || (isValidOtherName == false)) {
			return;
		}

		boolean result = this.phoneServer_.startConnection(ownName, otherName);

		// 接続処理に失敗した場合
		if (result == false) {
			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			String errMsg = otherName + " への接続に失敗しました.";
			this.phoneFrame_.appendText(errMsg);
			this.otherPhoneName_ = null;

			return;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);
		this.otherPhoneName_ = otherName;

		// 相手に接続が成功したメッセージを送信
		String message = otherName + " に接続しました.";
		this.phoneFrame_.appendText(message);
	}

	/**
	 * 他携帯電話アプリとの接続を解除する。
	 * 
	 */
	public synchronized void terminateConnectiton() {
		String ownName = this.phoneFrame_.getOwnName();
		String otherName = this.phoneFrame_.getConnectName();

		boolean isValidOwnName = this.checkName(ownName);
		boolean isValidOtherName = this.checkName(otherName);
		if ((isValidOwnName == false) || (isValidOtherName == false)) {
			return;
		}

		boolean isDisConnected = this.phoneServer_.terminateConnection(ownName,
				otherName);

		if (isDisConnected == false) {
			String errMsg = "接続を中断できませんでした.";
			this.phoneFrame_.appendText(errMsg);

			this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);
			return;
		}

		String message = otherName + " との接続を中断しました.";
		this.phoneFrame_.appendText(message);
		this.otherPhoneName_ = null;

		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
	}

	/**
	 * メッセージを送信する。
	 * 
	 */
	public synchronized void sendMessage() {
		String message = this.phoneFrame_.getSendMessage();
		boolean isValidMsg = this.checkName(message);

		// 空文字列の場合は送信しない
		if (isValidMsg == false) {
			return;
		}

		String ownName = this.phoneFrame_.getOwnName();
		String otherName = this.phoneFrame_.getConnectName();

		boolean isValidOwnName = this.checkName(ownName);
		boolean isValidOtherName = this.checkName(otherName);
		if ((isValidOwnName == false) || (isValidOtherName == false)) {
			return;
		}

		boolean isSent = this.phoneServer_.sendMessage(ownName, otherName,
				message);

		if (isSent == false) {
			String errMsg = "メッセージの送信に失敗しました.";
			this.showErrorMessage(errMsg);
			return;
		}

		this.phoneFrame_.appendText(message);
	}

	/**
	 * メッセージを受信する。
	 * 
	 * @param message
	 *            受信メッセージ
	 */
	public synchronized boolean recieveMessage(String senderName, String message) {
		String otherName = this.phoneFrame_.getConnectName();
		boolean isValid = this.checkName(otherName);
		boolean isValidArg = this.checkName(senderName);

		if ((isValid == false) || (isValidArg == false)) {
			return false;
		}

		if (otherName.trim().equals(senderName.trim()) == false) {
			String errMsg = "メッセージの送信に失敗しました.";
			this.showErrorMessage(errMsg);
			return false;
		}

		this.phoneFrame_.appendText(message);

		return true;
	}

	/**
	 * エラーメッセージ表示処理を行う。
	 * 
	 * @param errMsg
	 *            エラーメッセージ
	 */
	public synchronized void showErrorMessage(String errMsg) {
		this.phoneFrame_.showErrorDialog(errMsg);
	}

	/**
	 * エラーログを出力する。
	 * 
	 * @param errMsg
	 *            エラーメッセージ
	 */
	public synchronized void errorLog(String errMsg) {
		System.err.println(errMsg);
	}

	/**
	 * ポート番号文字列から数値を取得する。
	 * 
	 * 指定された文字列が数値でない、またはポート番号として不適切な場合は 不正値であることを示す値(INVALID_PORT_NUMBER)を返す。
	 * 
	 * @param portStr
	 *            ポート番号文字列
	 * @return ポート番号
	 */
	private int getPortNumberFromStr(String portStr) {
		int portNumber = INVALID_PORT_NUMBER;

		try {
			portNumber = Integer.parseInt(portStr);

			if ((portNumber < MINMUM_PORT_NUMBER)
					|| (MAXMUM_PORT_NUMBER < portNumber)) {
				portNumber = INVALID_PORT_NUMBER;
			}
		} catch (NumberFormatException nfExc) {
			this.errorLog("数値への変換に失敗");
		}

		return portNumber;
	}

	/**
	 * ORBを初期化する。
	 * 
	 * @param namingSeviceHost
	 *            ネーミングサービスのホスト
	 * @param namingServicePort
	 *            ネーミングサービスのポート
	 * @return
	 */
	private ORB initOrb(String namingSeviceHost, String namingServicePort) {
		Properties property = new Properties();
		property.put(PhoneConstant.ORB_HOST_KEY, namingSeviceHost);
		property.put(PhoneConstant.ORB_PORT_KEY, namingServicePort);

		String[] initArgs = null;
		// ORBの生成と初期化を行う
		ORB orb = ORB.init(initArgs, property);

		return orb;
	}

	/**
	 * 名前が適切なものかチェックする。
	 * 
	 * @param name
	 *            名前
	 * @return 適切ならばtrue, 不適切ならばfalse
	 */
	private boolean checkName(String name) {
		if (name == null) {
			return false;
		}

		boolean isValid = !("".equals(name.trim()));

		return isValid;
	}
}
