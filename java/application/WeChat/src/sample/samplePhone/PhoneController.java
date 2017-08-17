package sample.samplePhone;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

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
	private static final int MINMUM_PORT_NUMBER = 1024;

	/** ポート番号として設定できる値の最大値。 */
	private static final int MAXMUM_PORT_NUMBER = 65535;

	/** 初期化処理が行われたか判断するフラグ。 */
	private boolean isInitialized_;

	/** ソケット受信スレッド。 */
	private SocketConnector socketConnector_;

	/** メッセージ受信オブジェクト(マルチ)。 */
	private ArrayList<MessageReciever> msgRecieverList_ = new ArrayList<MessageReciever>();

	/** メッセージ送信オブジェクト(マルチ)。 */
	private ArrayList<MessageSender> msgSenderList_ = new ArrayList<MessageSender>();

	/** 携帯電話ウィンドウ。 */
	private PhoneFrame phoneFrame_;

	/** 携帯電話P2Pモード。 */
	private boolean phoneP2PMode_;

	/** グループホストモード。 */
	private boolean phoneGroupMode_;

	/** 携帯電話接続数。 */
	protected static int numConnection_;

	/** 携帯電話通常終了フラグ。 */
	private boolean normalDisconnect_;

	/** クライアント通信から住所関連 */
	private HashMap<MessageReciever, Socket> clientReciverAdressMap_ = new HashMap<>();

	/**
	 * コンストラクタ。 コンストラクタ実行後、初期化メソッドinitialize()を実行する必要がある。
	 * 
	 */
	public PhoneController() {
		// フィールドの初期化
		this.phoneFrame_ = new PhoneFrame(this);
		this.isInitialized_ = false;
		this.phoneP2PMode_ = PhoneConstant.NON_INTERRUPT_MODE;
		this.phoneGroupMode_ = false;
		this.normalDisconnect_ = false;
		PhoneController.numConnection_ = 0;
	}

	public SocketConnector getSocketConnector_() {
		return socketConnector_;
	}

	public ArrayList<MessageReciever> getMsgRecieverList_() {
		return msgRecieverList_;
	}

	public ArrayList<MessageSender> getMsgSenderList_() {
		return msgSenderList_;
	}

	public HashMap<MessageReciever, Socket> getClientReciverAdressMap_() {
		return clientReciverAdressMap_;
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

	public boolean isPhoneP2PMode_() {
		return phoneP2PMode_;
	}

	public void setPhoneP2PMode_(boolean phoneP2PMode_) {
		this.phoneP2PMode_ = phoneP2PMode_;
	}

	public boolean isPhoneGroupMode_() {
		return phoneGroupMode_;
	}

	public void setPhoneGroupMode_(boolean phoneGroupMode_) {
		this.phoneGroupMode_ = phoneGroupMode_;
	}

	public boolean isNormalDisconnect_() {
		return normalDisconnect_;
	}

	public void setNormalDisconnect_(boolean normalDisconnect_) {
		this.normalDisconnect_ = normalDisconnect_;
	}

	/**
	 * 電源をオンにし、ソケット受信処理を開始する。
	 * 
	 */
	public void startPhoneServer() {
		String ownPortStr = this.phoneFrame_.getOwnPortStr();
		int ownPortNumber = this.getPortNumberFromStr(ownPortStr);

		if (ownPortNumber == INVALID_PORT_NUMBER) {
			String errMsg = "自ポート番号は " + MINMUM_PORT_NUMBER + " ～ "
					+ MAXMUM_PORT_NUMBER + " の整数値を指定してください.";
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			this.showErrorMessage(errMsg);

			return;
		}

		try {
			this.socketConnector_ = SocketConnector.createConnector(this,
					ownPortNumber);
		} catch (IOException ioExc) {
			String errMsg = "ポートの確保に失敗しました.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			this.socketConnector_ = null;

			return;
		}

		Thread msgRecieveThread = new Thread(this.socketConnector_);
		msgRecieveThread.start();

		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
		this.phoneFrame_.appendText("電源をオンにしました.");
	}

	public void startPhoneServer(int groupHostPort) {

		if (groupHostPort == INVALID_PORT_NUMBER) {
			String errMsg = "自ポート番号は " + MINMUM_PORT_NUMBER + " ～ "
					+ MAXMUM_PORT_NUMBER + " の整数値を指定してください.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);

			return;
		}

		try {
			this.socketConnector_ = SocketConnector.createConnector(this,
					groupHostPort);
		} catch (IOException ioExc) {
			String errMsg = "ポートの確保に失敗しました.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			this.socketConnector_ = null;

			return;
		}

		Thread msgRecieveThread = new Thread(this.socketConnector_);
		msgRecieveThread.start();

		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
		this.phoneFrame_.appendText("グループホストになりました");
		InetAddress ia;
		try {
			ia = InetAddress.getLocalHost();
			String ip = ia.getHostAddress(); // IPアドレス
			String hostname = ia.getHostName(); // ホスト名
			this.phoneFrame_.appendText("ホスト名：" + ip + "(" + hostname + ")");
			this.phoneFrame_.appendText("ポート(固定)：" + groupHostPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 電源をオフにする。
	 * 
	 */
	public synchronized void terminatePhoneServer() {
		// 既にサーバが終了している場合、何もしない
		if (this.socketConnector_ == null) {
			return;
		}

		this.closeConnectedSocketStream();

		this.socketConnector_.terminate();
		this.socketConnector_ = null;
		this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
	}

	/**
	 * ソケットを受信した時に呼び出されるメソッド。
	 * 
	 * @param socket
	 *            ストリーム生成対象のソケット
	 * @return 接続結果
	 */
	public synchronized boolean acceptSocket(Socket socket) {
		PhoneController.numConnection_ += 1;
		try {
			MessageReciever msgReciever = MessageReciever.createReciever(this,
					socket);
			this.msgRecieverList_.add(msgReciever);
			MessageSender msgSender = MessageSender.createSender(this, socket);
			this.msgSenderList_.add(msgSender);

			this.clientReciverAdressMap_.put(msgReciever, socket);

			// ソケット受信中設定
			this.socketConnector_.setRecievedNow(true);

			Thread recieveThread = new Thread(msgReciever);
			recieveThread.start();

			// 画面描画
			this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);

			// グループモード
			if (this.phoneGroupMode_) {
				// 相手に接続が成功したメッセージを送信
				String message;
				try {
					message = InetAddress.getLocalHost().getHostName()
							+ "に接続しました.";
					for (MessageSender msgSenderMemb : this.msgSenderList_) {
						msgSenderMemb.sendMessage(message);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				return true;
			}

			// 相手に接続が成功したメッセージを送信
			String message = "接続しました.";
			msgSender.sendMessage(message);

			return true;
		} catch (IOException ioExc) {
			this.showErrorMessage("ストリームの取得に失敗しました.");
			this.msgRecieverList_.add(null);
			this.msgSenderList_.add(null);
			return false;
		}
	}

	/**
	 * 他携帯電話アプリに接続する。
	 * 
	 */
	public synchronized void startConnection() {
		String portStr = this.phoneFrame_.getConnectPortStr();
		int portNumber = this.getPortNumberFromStr(portStr);

		if (portNumber == INVALID_PORT_NUMBER) {
			String errMsg = "接続先ポート番号は " + MINMUM_PORT_NUMBER + " ～ "
					+ MAXMUM_PORT_NUMBER + " の整数値を指定してください.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			return;
		}

		String hostStr = this.phoneFrame_.getConnectHostStr();
		InetAddress address = null;

		try {
			address = InetAddress.getByName(hostStr);
		} catch (UnknownHostException uhExc) {
			this.showErrorMessage("接続先が見つかりません.");
			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			return;
		}

		Socket connectSocket = this.connectServer(address, portNumber);

		// 接続処理に失敗した場合
		if (connectSocket == null) {
			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			return;
		}

		boolean isAccepted = this.acceptSocket(connectSocket);

		// ストリームの取得に失敗した場合
		if (isAccepted == false) {
			this.errorLog("受信したソケットを破棄します.");
			String errMsg = "ストリーム取得中に異常が発生した為、接続を中断します.";
			this.cancelNewConnection(connectSocket, errMsg);

			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			return;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);
	}

	/**
	 * 他携帯電話アプリとの接続を解除する。
	 * 
	 */
	public synchronized void terminateConnection() {
		if (PhoneController.numConnection_ > 0) {
			PhoneController.numConnection_ -= 1;
		}
		this.closeConnectedSocketStream();
		if (this.socketConnector_ != null
				&& PhoneController.numConnection_ == 0) {
			this.socketConnector_.setRecievedNow(false);
		}

		// 通常終了フラグを初期化する
		this.setNormalDisconnect_(false);
		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
	}

	/**
	 * 他携帯電話アプリとの接続を解除する。
	 * 
	 */
	public synchronized void terminateConnection(MessageReciever msgReciever) {
		if (PhoneController.numConnection_ > 0) {
			PhoneController.numConnection_ -= 1;
		}
		this.closeConnectedSocketStream(msgReciever);
		if (this.socketConnector_ != null
				&& PhoneController.numConnection_ == 0) {
			this.socketConnector_.setRecievedNow(false);
		}

		// 通常終了フラグを初期化する
		this.setNormalDisconnect_(false);
		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
	}

	/**
	 * メッセージを送信する。
	 * 
	 */
	public synchronized void sendMessage() {
		String message = this.phoneFrame_.getSendMessage();

		// 空文字列の場合は送信しない
		if ((message == null) || ("".equals(message.trim()) == true)) {
			return;
		}

		for (MessageSender msgSender_ : this.msgSenderList_) {
			if (msgSender_ == null) {
				continue;
			}
			boolean isSent = msgSender_.sendMessage(message);
			if (isSent == false) {
				this.terminateConnection();
				return;
			}
		}

		try {
			this.phoneFrame_.appendText(new String(message
					.getBytes(PhoneConstant.ENCODE_UTF_8),
					PhoneConstant.ENCODE_UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.phoneFrame_.clearMsg();
	}

	/**
	 * メッセージを受信する。
	 * 
	 * @param message
	 *            受信メッセージ
	 */
	public synchronized void recieveMessage(String message) {
		try {
			this.phoneFrame_.appendText(new String(message
					.getBytes(PhoneConstant.ENCODE_UTF_8),
					PhoneConstant.ENCODE_UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * エラーメッセージ表示処理を行う。
	 * 
	 * @param errMsg
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
	 * 受信したソケットとの接続を破棄する。
	 * 
	 * @param socket
	 *            接続を破棄するソケット
	 * @param sendMsg
	 *            受信したソケットに送信するメッセージ
	 */
	public void cancelNewConnection(Socket socket, String sendMsg) {
		BufferedWriter buffWriter = null;

		try {
			OutputStream output = socket.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(output,
					PhoneConstant.ENCODE_UTF_8);
			buffWriter = new BufferedWriter(writer);
			buffWriter.write(sendMsg);
		} catch (IOException ioExc) {
			// 相手に接続不可能であることを送信できなかった
			this.showErrorMessage("接続終了メッセージ送信失敗.");
		} finally {
			if (buffWriter != null) {
				try {
					buffWriter.close();
				} catch (IOException ioExc) {
					// 何もしない
					ioExc.printStackTrace();
				}
			}
			try {
				socket.close();
			} catch (IOException ioExc) {
				// 何もしない
				ioExc.printStackTrace();
			}
		}
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
			System.err.println("数値への変換に失敗");
		}

		return portNumber;
	}

	/**
	 * サーバに接続処理を行う。 接続に失敗した場合はnullを返す。
	 * 
	 * @param address
	 *            接続先ホスト
	 * @param portNumber
	 *            接続先ポート
	 * @return 接続に成功したソケット
	 */
	private Socket connectServer(InetAddress address, int portNumber) {
		Socket connectSocket = new Socket();

		try {
			// 接続先ホストとポートを指定してサーバに接続する
			InetSocketAddress socketAddress = new InetSocketAddress(address,
					portNumber);
			connectSocket.connect(socketAddress,
					PhoneConstant.TIMEOUT_SOCKET_CONNECT);
		} catch (SocketTimeoutException timeoutExc) {
			this.showErrorMessage("タイムアウトにより接続できませんでした.");
			connectSocket = null;
		} catch (IOException ioExc) {
			this.showErrorMessage("接続に失敗しました.");
			connectSocket = null;
		}

		return connectSocket;
	}

	/**
	 * 現在受信中のソケットの接続を閉じる。
	 * 
	 */
	private void closeConnectedSocketStream() {
		if (this.msgSenderList_ != null) {
			for (MessageSender msgSender : this.msgSenderList_) {
				msgSender.sendMessage("接続を解除しました.");
				msgSender.terminate();
				msgSender = null;
			}
			this.msgSenderList_.clear();
		}

		if (this.msgRecieverList_ != null) {
			for (MessageReciever msgReciever : this.msgRecieverList_) {
				msgReciever.terminate();
				msgReciever = null;
			}
			this.msgRecieverList_.clear();
		}

		PhoneController.numConnection_ = 0;

		if (this.clientReciverAdressMap_ != null) {
			try {
				for (Socket connectedSocket : this.clientReciverAdressMap_
						.values()) {
					connectedSocket.close();
				}
			} catch (IOException ioExc) {
				// 何もしない
			} finally {
				this.clientReciverAdressMap_.clear();
			}
		}
	}

	/**
	 * 現在受信中のソケットの接続を閉じる。
	 * 
	 */
	private void closeConnectedSocketStream(MessageReciever msgReciever) {
		int numbOfRecandSen = this.msgRecieverList_.indexOf(msgReciever);
		if (this.msgSenderList_.get(numbOfRecandSen) != null) {
			MessageSender msgSender = this.msgSenderList_.get(numbOfRecandSen);
			msgSender.sendMessage("接続を解除しました.");
			msgSender.terminate();
			this.msgSenderList_.remove(msgSender);
		}

		if (this.msgRecieverList_.get(numbOfRecandSen) != null) {
			this.msgRecieverList_.get(numbOfRecandSen).terminate();
			this.msgRecieverList_.remove(numbOfRecandSen);
		}

		if (this.clientReciverAdressMap_.get(msgReciever) != null) {
			try {
				this.clientReciverAdressMap_.get(msgReciever).close();
			} catch (IOException ioExc) {
				// 何もしない
			} finally {
				this.clientReciverAdressMap_.remove(msgReciever);
			}
		}

	}

	public void toggleConnentionStatus() {
		if (PhoneController.numConnection_ > 0) {
			this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);
		} else if (PhoneController.numConnection_ <= 0) {
			if (this.socketConnector_ == null) {
				String errMsg = "自ポート番号は " + MINMUM_PORT_NUMBER + " ～ "
						+ MAXMUM_PORT_NUMBER + " の整数値を指定してください.";
				this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
				this.showErrorMessage(errMsg);
			} else {
				this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			}
		}
	}

}
