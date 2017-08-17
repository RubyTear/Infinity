package sample.samplePhone;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

/**
 * メッセージ受信オブジェクト。
 * 
 * @author Acroquest
 * 
 */
public final class MessageReciever implements Runnable {
	/** 携帯電話アプリ管理オブジェクト。 */
	private PhoneController controller_;

	/** メッセージ受信バッファ。 */
	private BufferedReader buffReader_;

	/** 受信処理が終了したか判断するフラグ。 */
	private boolean isReading_;

	/**
	 * 外部から呼び出し不可能なコンストラクタ。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 * @param bufferedReader
	 *            バッファリーダ
	 */
	private MessageReciever(PhoneController controller,
			BufferedReader bufferedReader) {
		// フィールドの初期化
		this.controller_ = controller;
		this.buffReader_ = bufferedReader;
		this.isReading_ = true;
	}

	/**
	 * メッセージ受信オブジェクトを生成する。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 * @param socket
	 *            メッセージを受信するソケット
	 * @return メッセージ受信オブジェクト
	 * @throws IOException
	 *             入力ストリームの取得に失敗した場合にスローする
	 */
	public static MessageReciever createReciever(PhoneController controller,
			Socket socket) throws IOException {
		InputStream inputStream = socket.getInputStream();
		InputStreamReader inputReader = new InputStreamReader(inputStream,
				PhoneConstant.ENCODE_UTF_8);
		BufferedReader bufferedReader = new BufferedReader(inputReader);

		MessageReciever returnReciever = new MessageReciever(controller,
				bufferedReader);
		return returnReciever;
	}

	/**
	 * ソケットからメッセージを常時受信する処理。
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		String message = null;

		while (this.getReading() == true) {
			// 入力ストリームが接続されていない場合、切断処理を行う
			if (this.buffReader_ == null) {
				if (this.controller_.isNormalDisconnect_() == false) {
					this.controller_.terminateConnection();
					break;
				}
			}

			try {
				message = this.buffReader_.readLine();
			} catch (IOException ioExc) {
				// 接続を異常終了する
				if (this.controller_.isNormalDisconnect_() == false) {
					this.controller_.terminateConnection();
					break;
				}
			}

			// 相手側から接続が切れた場合
			if (message == null) {
				this.controller_.setNormalDisconnect_(true);
				this.controller_.terminateConnection(this);
				break;
			}

			// グループモードで他人(orther Socket)に送信する。
			if (this.controller_.isPhoneGroupMode_()) {
				ArrayList<MessageSender> msgSenderList_ = this.controller_
						.getMsgSenderList_();
				int skipClientNum = this.controller_.getMsgRecieverList_()
						.indexOf(this);

				for (int i = 0; i < msgSenderList_.size(); i++) {
					if (i == skipClientNum) {
						continue;
					}
					msgSenderList_.get(i).sendMessage(message);
				}
			}

			this.controller_.recieveMessage(message);
		}

		// 終了処理を行う
		if (this.buffReader_ != null) {
			try {
				this.buffReader_.close();
			} catch (IOException ioExc) {
				// 何もしない
			} finally {
				this.buffReader_ = null;
			}
		}

		// 送信が終了したら正常終了フラグを初期化する。
		// this.controller_.setNormalDisconnect_(false);
	}

	/**
	 * 終了状態に設定する。
	 * 
	 */
	public synchronized void terminate() {
		this.isReading_ = false;
	}

	/**
	 * メッセージ受信状態が終了か調べる。 trueならば受信する状態、falseならば受信しない状態。
	 * 
	 * @return メッセージ受信状態
	 */
	public synchronized boolean getReading() {
		return this.isReading_;
	}

}
