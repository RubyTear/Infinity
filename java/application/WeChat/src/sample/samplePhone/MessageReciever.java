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
 * ���b�Z�[�W��M�I�u�W�F�N�g�B
 * 
 * @author Acroquest
 * 
 */
public final class MessageReciever implements Runnable {
	/** �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g�B */
	private PhoneController controller_;

	/** ���b�Z�[�W��M�o�b�t�@�B */
	private BufferedReader buffReader_;

	/** ��M�������I�����������f����t���O�B */
	private boolean isReading_;

	/**
	 * �O������Ăяo���s�\�ȃR���X�g���N�^�B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 * @param bufferedReader
	 *            �o�b�t�@���[�_
	 */
	private MessageReciever(PhoneController controller,
			BufferedReader bufferedReader) {
		// �t�B�[���h�̏�����
		this.controller_ = controller;
		this.buffReader_ = bufferedReader;
		this.isReading_ = true;
	}

	/**
	 * ���b�Z�[�W��M�I�u�W�F�N�g�𐶐�����B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 * @param socket
	 *            ���b�Z�[�W����M����\�P�b�g
	 * @return ���b�Z�[�W��M�I�u�W�F�N�g
	 * @throws IOException
	 *             ���̓X�g���[���̎擾�Ɏ��s�����ꍇ�ɃX���[����
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
	 * �\�P�b�g���烁�b�Z�[�W���펞��M���鏈���B
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		String message = null;

		while (this.getReading() == true) {
			// ���̓X�g���[�����ڑ�����Ă��Ȃ��ꍇ�A�ؒf�������s��
			if (this.buffReader_ == null) {
				if (this.controller_.isNormalDisconnect_() == false) {
					this.controller_.terminateConnection();
					break;
				}
			}

			try {
				message = this.buffReader_.readLine();
			} catch (IOException ioExc) {
				// �ڑ����ُ�I������
				if (this.controller_.isNormalDisconnect_() == false) {
					this.controller_.terminateConnection();
					break;
				}
			}

			// ���葤����ڑ����؂ꂽ�ꍇ
			if (message == null) {
				this.controller_.setNormalDisconnect_(true);
				this.controller_.terminateConnection(this);
				break;
			}

			// �O���[�v���[�h�ő��l(orther Socket)�ɑ��M����B
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

		// �I���������s��
		if (this.buffReader_ != null) {
			try {
				this.buffReader_.close();
			} catch (IOException ioExc) {
				// �������Ȃ�
			} finally {
				this.buffReader_ = null;
			}
		}

		// ���M���I�������琳��I���t���O������������B
		// this.controller_.setNormalDisconnect_(false);
	}

	/**
	 * �I����Ԃɐݒ肷��B
	 * 
	 */
	public synchronized void terminate() {
		this.isReading_ = false;
	}

	/**
	 * ���b�Z�[�W��M��Ԃ��I�������ׂ�B true�Ȃ�Ύ�M�����ԁAfalse�Ȃ�Ύ�M���Ȃ���ԁB
	 * 
	 * @return ���b�Z�[�W��M���
	 */
	public synchronized boolean getReading() {
		return this.isReading_;
	}

}
