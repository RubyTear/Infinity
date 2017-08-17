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
 * �O������̃\�P�b�g�ڑ����󂯕t����N���X�B
 * 
 * @author Acroquest
 * 
 */
public final class SocketConnector implements Runnable {
	/** �g�ѓd�b�A�v���̐���N���X�B */
	private PhoneController controller_;

	/** �T�[�o�\�P�b�g�B */
	volatile private ServerSocket serverSocket_;

	/** �N���C�A���g����̐ڑ�����M�������f����t���O�B */
	private boolean isRecievedNow_;

	/** �I���v���������������f����t���O�B */
	private boolean isTermination_;

	/**
	 * �O������Ăяo���s�\�ȃR���X�g���N�^�B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 */
	private SocketConnector(PhoneController controller) {
		this.controller_ = controller;
	}

	/**
	 * �N���C�A���g��M�R�l�N�^�𐶐�����B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 * @param port
	 *            ���|�[�g�ԍ�
	 * @return �\�P�b�g��M�R�l�N�^
	 * @throws IOException
	 *             �T�[�o�\�P�b�g�̐����Ɏ��s�����ꍇ�ɃX���[����
	 */
	public static SocketConnector createConnector(PhoneController controller,
			int port) throws IOException {
		SocketConnector connector = new SocketConnector(controller);
		connector.initSocketServer(port);

		return connector;
	}

	/**
	 * �N���C�A���g����̐ڑ����펞�Ď����鏈���B
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// �I���v���������Ԃ̓��[�v����
		while (this.isTerminatedRequest() == false) {
			// �ڑ���M�������s��
			Socket connectedSocket = this.acceptNewConnection();

			// �ڑ�����M���Ă��Ȃ��ꍇ�A�Ď�M�����ɖ߂�
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
			// �I���������s��
			try {
				this.serverSocket_.close();
			} catch (IOException ioExc) {
				// �������Ȃ�
			} finally {
				this.serverSocket_ = null;
			}
		}
	}

	/**
	 * �����_�ŃN���C�A���g�Ɛڑ������̏����擾����B
	 * 
	 * @return �ڑ����t���O
	 */
	public synchronized boolean isRecievedNow() {
		return this.isRecievedNow_;
	}

	/**
	 * �N���C�A���g�Ɛڑ�������ݒ肷��B
	 * 
	 * @param isRecieved
	 *            �ڑ����t���O
	 */
	public synchronized void setRecievedNow(boolean isRecieved) {
		this.isRecievedNow_ = isRecieved;
	}

	/**
	 * �I��������v������B
	 * 
	 */
	public synchronized void terminate() {
		this.isTermination_ = true;
	}

	/**
	 * �I���v���������������f����B
	 * 
	 * @return �I���v���𔻒f����t���O
	 */
	public synchronized boolean isTerminatedRequest() {
		return this.isTermination_;
	}

	/**
	 * �T�[�o�\�P�b�g�̏��������s���B
	 * 
	 * @param port
	 *            �|�[�g�ԍ�
	 * @throws IOException
	 *             �T�[�o�\�P�b�g�쐬�Ɏ��s�����ꍇ
	 */
	private void initSocketServer(int port) throws IOException {
		this.serverSocket_ = new ServerSocket(port);
		// �^�C���A�E�g�����Server��Null Client���󂯎�邽��
		this.serverSocket_.setSoTimeout(PhoneConstant.TIMEOUT_SERVER_SOCKET);
	}

	/**
	 * �N���C�A���g����̐ڑ����󂯕t����B ��M�����\�P�b�g��j������ꍇ��null��Ԃ��B
	 * 
	 * @return �\�P�b�g
	 */
	private Socket acceptNewConnection() {
		Socket connectedSocket = null;

		try {
			connectedSocket = this.serverSocket_.accept();
		} catch (SocketTimeoutException timeoutExc) {
			// �������Ȃ�
		} catch (IOException ioExc) {
			this.controller_.errorLog("�\�P�b�g��M���Ɉُ픭��");
		}

		// ���݊��ɒʐM���̏ꍇ�́A���̐ڑ���j������
		if ((connectedSocket != null)
				&& (this.isRecievedNow() == true)
				&& (this.controller_.isPhoneP2PMode_() || this.controller_
						.isPhoneGroupMode_() == false)) {
			String errMsg;
			try {
				errMsg = new String(
						"���ɒʐM���̂��߁A�ڑ��ł��܂���."
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
