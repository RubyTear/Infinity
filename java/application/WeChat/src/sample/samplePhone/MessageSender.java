package sample.samplePhone;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * メッセージ送信オブジェクト。
 * 
 * @author Acroquest
 * 
 */
public final class MessageSender {
	/** 携帯電話アプリ管理オブジェクト。 */
	private PhoneController controller_;

	/** メッセージ受信バッファ。 */
	private BufferedWriter buffWriter_;

	/** 送信メッセージの頭につける文字列。 */
	private final String header_;

	/**
	 * 外部からは呼び出し不可能なコンストラクタ。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 * @param buffWriter
	 *            バッファライタ
	 */
	private MessageSender(PhoneController controller, BufferedWriter buffWriter) {
		this.controller_ = controller;
		this.buffWriter_ = buffWriter;

		String senderName = "\t\t";

		// ローカルホストの取得
		try {
			InetAddress localAddress = InetAddress.getLocalHost();
			senderName += localAddress.getHostName();
		} catch (UnknownHostException uhExc) {
			this.controller_.errorLog("ローカルホストアドレスの取得に失敗.");
		}

		this.header_ = senderName + "#";
	}

	/**
	 * メッセージ送信オブジェクトを生成する。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 * @param socket
	 *            受信ソケット
	 * @return メッセージ送信オブジェクト
	 * @throws IOException
	 *             出力ストリームの取得に失敗した場合に取得する
	 */
	public static MessageSender createSender(PhoneController controller,
			Socket socket) throws IOException {
		OutputStream output = socket.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(output,
				PhoneConstant.ENCODE_UTF_8);
		BufferedWriter buffWriter = new BufferedWriter(writer);

		MessageSender messageSender = new MessageSender(controller, buffWriter);
		return messageSender;
	}

	/**
	 * メッセージを送信する。
	 * 
	 * @param message
	 *            送信メッセージ
	 * @return メッセージ送信に成功した場合はtrue, 失敗した場合はfalse
	 */
	public boolean sendMessage(String message) {
		if (this.buffWriter_ == null) {
			return false;
		}

		boolean isNormalWork = true;
		try {
			if (message.contains(this.header_)) {
				this.buffWriter_.write(message);
			} else {
				this.buffWriter_.write(this.header_ + message);
			}
			this.buffWriter_.newLine();
			this.buffWriter_.flush();
		} catch (IOException ioExc) {
			isNormalWork = false;
		}

		return isNormalWork;
	}

	/**
	 * 接続終了処理を行う。
	 * 
	 */
	public void terminate() {
		if (this.buffWriter_ == null) {
			return;
		}

		try {
			this.buffWriter_.close();
		} catch (IOException ioExc) {
			// 何もしない
		} finally {
			this.buffWriter_ = null;
		}
	}
}
