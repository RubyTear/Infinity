package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

/**
 * 携帯電話アプリクライアントからの要求をサーバで実行するスケルトンクラス。
 * 
 * @author Acroquest
 * 
 */
public class HandyPhoneImpl extends HandyPhoneIFPOA {
	/** 携帯電話アプリ管理オブジェクト。 */
	private PhoneController controller_;

	/**
	 * コンストラクタ。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 */
	public HandyPhoneImpl(PhoneController controller) {
		this.controller_ = controller;
	}

	/**
	 * クライアント側では、このメソッドはメッセージを受信した時に呼び出される。
	 * 
	 * @param senderName
	 *            送信者名
	 * @param message
	 *            送信メッセージ
	 * @return メッセージの受信に成功した場合はtrue, 失敗した場合はfalse
	 * @see sample.corba.handyphone.HandyPhoneIFOperations
	 *      #sendMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendMessage(String senderName, String message) {
		String recievedMsg = senderName + "#" + message;
		boolean isSent = this.controller_.recieveMessage(senderName,
				recievedMsg);
		return isSent;
	}

	/**
	 * クライアント側では、このメソッドは接続要求を受信した時に呼び出される。
	 * 
	 * @param senderName
	 *            送信者名
	 * @param recieverName
	 *            受信者名
	 * @return 接続に成功した場合はtrue, 失敗した場合はfalse
	 * @see sample.corba.handyphone.HandyPhoneIFOperations
	 *      #connect(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean connect(String senderName, String recieverName) {
		boolean isConnected = this.controller_.acceptConnection(senderName,
				recieverName);

		return isConnected;
	}

	/**
	 * クライアントでは、このメソッドは接続遮断要求を受信した時に呼び出される。
	 * 
	 * @param senderName
	 *            送信者名
	 * @return 接続遮断に成功した場合はtrue, 失敗した場合はfalse
	 * @see sample.corba.handyphone.HandyPhoneIFOperations
	 *      #disConnect(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean disConnect(String senderName, String recieverName) {
		boolean isDisConnected = this.controller_.acceptDisConnection(
				senderName, recieverName);

		return isDisConnected;
	}
}
