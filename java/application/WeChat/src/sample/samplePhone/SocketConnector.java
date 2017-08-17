package sample.samplePhone;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * 外部からのソケット接続を受け付けるクラス。
 * 
 * @author Acroquest
 * 
 */
public final class SocketConnector implements Runnable {
	/** 携帯電話アプリの制御クラス。 */
	private PhoneController controller_;

	/** サーバソケット。 */
	volatile private ServerSocket serverSocket_;

	/** クライアントからの接続を受信中か判断するフラグ。 */
	private boolean isRecievedNow_;

	/** 終了要求があったか判断するフラグ。 */
	private boolean isTermination_;

	/**
	 * 外部から呼び出し不可能なコンストラクタ。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 */
	private SocketConnector(PhoneController controller) {
		this.controller_ = controller;
	}

	/**
	 * クライアント受信コネクタを生成する。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 * @param port
	 *            自ポート番号
	 * @return ソケット受信コネクタ
	 * @throws IOException
	 *             サーバソケットの生成に失敗した場合にスローする
	 */
	public static SocketConnector createConnector(PhoneController controller,
			int port) throws IOException {
		SocketConnector connector = new SocketConnector(controller);
		connector.initSocketServer(port);

		return connector;
	}

	/**
	 * クライアントからの接続を常時監視する処理。
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// 終了要求が無い間はループする
		while (this.isTerminatedRequest() == false) {
			// 接続受信処理を行う
			Socket connectedSocket = this.acceptNewConnection();

			// 接続を受信していない場合、再受信処理に戻る
			if (connectedSocket == null) {
				continue;
			}

			boolean isAcecpted = this.controller_.acceptSocket(connectedSocket);

			if (isAcecpted == false) {
				this.setRecievedNow(false);
				continue;
			}
		}

		if (this.serverSocket_ != null) {
			// 終了処理を行う
			try {
				this.serverSocket_.close();
			} catch (IOException ioExc) {
				// 何もしない
			} finally {
				this.serverSocket_ = null;
			}
		}
	}

	/**
	 * 現時点でクライアントと接続中かの情報を取得する。
	 * 
	 * @return 接続中フラグ
	 */
	public synchronized boolean isRecievedNow() {
		return this.isRecievedNow_;
	}

	/**
	 * クライアントと接続中かを設定する。
	 * 
	 * @param isRecieved
	 *            接続中フラグ
	 */
	public synchronized void setRecievedNow(boolean isRecieved) {
		this.isRecievedNow_ = isRecieved;
	}

	/**
	 * 終了処理を要求する。
	 * 
	 */
	public synchronized void terminate() {
		this.isTermination_ = true;
	}

	/**
	 * 終了要求があったか判断する。
	 * 
	 * @return 終了要求を判断するフラグ
	 */
	public synchronized boolean isTerminatedRequest() {
		return this.isTermination_;
	}

	/**
	 * サーバソケットの初期化を行う。
	 * 
	 * @param port
	 *            ポート番号
	 * @throws IOException
	 *             サーバソケット作成に失敗した場合
	 */
	private void initSocketServer(int port) throws IOException {
		this.serverSocket_ = new ServerSocket(port);
		// タイムアウトするとServerがNull Clientを受け取るため
		this.serverSocket_.setSoTimeout(PhoneConstant.TIMEOUT_SERVER_SOCKET);
	}

	/**
	 * クライアントからの接続を受け付ける。 受信したソケットを破棄する場合はnullを返す。
	 * 
	 * @return ソケット
	 */
	private Socket acceptNewConnection() {
		Socket connectedSocket = null;

		try {
			connectedSocket = this.serverSocket_.accept();
		} catch (SocketTimeoutException timeoutExc) {
			// 何もしない
		} catch (IOException ioExc) {
			this.controller_.errorLog("ソケット受信時に異常発生");
		}

		// 現在既に通信中の場合は、この接続を破棄する
		if ((connectedSocket != null)
				&& (this.isRecievedNow() == true)
				&& (this.controller_.isPhoneP2PMode_() || this.controller_
						.isPhoneGroupMode_() == false)) {
			String errMsg;
			try {
				errMsg = new String(
						"既に通信中のため、接続できません."
								.getBytes(PhoneConstant.ENCODE_UTF_8),
						PhoneConstant.ENCODE_UTF_8);
				this.controller_.cancelNewConnection(connectedSocket, errMsg);
				connectedSocket = null;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return connectedSocket;
	}

}
