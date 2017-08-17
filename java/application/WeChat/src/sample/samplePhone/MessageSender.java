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
 * ���b�Z�[�W���M�I�u�W�F�N�g�B
 * 
 * @author Acroquest
 * 
 */
public final class MessageSender {
	/** �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g�B */
	private PhoneController controller_;

	/** ���b�Z�[�W��M�o�b�t�@�B */
	private BufferedWriter buffWriter_;

	/** ���M���b�Z�[�W�̓��ɂ��镶����B */
	private final String header_;

	/**
	 * �O������͌Ăяo���s�\�ȃR���X�g���N�^�B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 * @param buffWriter
	 *            �o�b�t�@���C�^
	 */
	private MessageSender(PhoneController controller, BufferedWriter buffWriter) {
		this.controller_ = controller;
		this.buffWriter_ = buffWriter;

		String senderName = "\t\t";

		// ���[�J���z�X�g�̎擾
		try {
			InetAddress localAddress = InetAddress.getLocalHost();
			senderName += localAddress.getHostName();
		} catch (UnknownHostException uhExc) {
			this.controller_.errorLog("���[�J���z�X�g�A�h���X�̎擾�Ɏ��s.");
		}

		this.header_ = senderName + "#";
	}

	/**
	 * ���b�Z�[�W���M�I�u�W�F�N�g�𐶐�����B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 * @param socket
	 *            ��M�\�P�b�g
	 * @return ���b�Z�[�W���M�I�u�W�F�N�g
	 * @throws IOException
	 *             �o�̓X�g���[���̎擾�Ɏ��s�����ꍇ�Ɏ擾����
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
	 * ���b�Z�[�W�𑗐M����B
	 * 
	 * @param message
	 *            ���M���b�Z�[�W
	 * @return ���b�Z�[�W���M�ɐ��������ꍇ��true, ���s�����ꍇ��false
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
	 * �ڑ��I���������s���B
	 * 
	 */
	public void terminate() {
		if (this.buffWriter_ == null) {
			return;
		}

		try {
			this.buffWriter_.close();
		} catch (IOException ioExc) {
			// �������Ȃ�
		} finally {
			this.buffWriter_ = null;
		}
	}
}
