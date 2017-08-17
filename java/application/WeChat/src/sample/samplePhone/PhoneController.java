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
 * �g�ѓd�b�A�v���̐���Ǘ����s���N���X�B
 * 
 * @author Acroquest
 * 
 */
public class PhoneController {
	/** �|�[�g�ԍ��ŕs���Ȓl����͂��ꂽ���ɕԂ��l�B */
	private static final int INVALID_PORT_NUMBER = -1;

	/** �|�[�g�ԍ��Ƃ��Đݒ�ł���l�̍ŏ��l�B */
	private static final int MINMUM_PORT_NUMBER = 1024;

	/** �|�[�g�ԍ��Ƃ��Đݒ�ł���l�̍ő�l�B */
	private static final int MAXMUM_PORT_NUMBER = 65535;

	/** �������������s��ꂽ�����f����t���O�B */
	private boolean isInitialized_;

	/** �\�P�b�g��M�X���b�h�B */
	private SocketConnector socketConnector_;

	/** ���b�Z�[�W��M�I�u�W�F�N�g(�}���`)�B */
	private ArrayList<MessageReciever> msgRecieverList_ = new ArrayList<MessageReciever>();

	/** ���b�Z�[�W���M�I�u�W�F�N�g(�}���`)�B */
	private ArrayList<MessageSender> msgSenderList_ = new ArrayList<MessageSender>();

	/** �g�ѓd�b�E�B���h�E�B */
	private PhoneFrame phoneFrame_;

	/** �g�ѓd�bP2P���[�h�B */
	private boolean phoneP2PMode_;

	/** �O���[�v�z�X�g���[�h�B */
	private boolean phoneGroupMode_;

	/** �g�ѓd�b�ڑ����B */
	protected static int numConnection_;

	/** �g�ѓd�b�ʏ�I���t���O�B */
	private boolean normalDisconnect_;

	/** �N���C�A���g�ʐM����Z���֘A */
	private HashMap<MessageReciever, Socket> clientReciverAdressMap_ = new HashMap<>();

	/**
	 * �R���X�g���N�^�B �R���X�g���N�^���s��A���������\�b�hinitialize()�����s����K�v������B
	 * 
	 */
	public PhoneController() {
		// �t�B�[���h�̏�����
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
	 * �����������B
	 * 
	 * @param frameSize
	 *            ��ʃT�C�Y
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
	 * �d�����I���ɂ��A�\�P�b�g��M�������J�n����B
	 * 
	 */
	public void startPhoneServer() {
		String ownPortStr = this.phoneFrame_.getOwnPortStr();
		int ownPortNumber = this.getPortNumberFromStr(ownPortStr);

		if (ownPortNumber == INVALID_PORT_NUMBER) {
			String errMsg = "���|�[�g�ԍ��� " + MINMUM_PORT_NUMBER + " �` "
					+ MAXMUM_PORT_NUMBER + " �̐����l���w�肵�Ă�������.";
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			this.showErrorMessage(errMsg);

			return;
		}

		try {
			this.socketConnector_ = SocketConnector.createConnector(this,
					ownPortNumber);
		} catch (IOException ioExc) {
			String errMsg = "�|�[�g�̊m�ۂɎ��s���܂���.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			this.socketConnector_ = null;

			return;
		}

		Thread msgRecieveThread = new Thread(this.socketConnector_);
		msgRecieveThread.start();

		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
		this.phoneFrame_.appendText("�d�����I���ɂ��܂���.");
	}

	public void startPhoneServer(int groupHostPort) {

		if (groupHostPort == INVALID_PORT_NUMBER) {
			String errMsg = "���|�[�g�ԍ��� " + MINMUM_PORT_NUMBER + " �` "
					+ MAXMUM_PORT_NUMBER + " �̐����l���w�肵�Ă�������.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);

			return;
		}

		try {
			this.socketConnector_ = SocketConnector.createConnector(this,
					groupHostPort);
		} catch (IOException ioExc) {
			String errMsg = "�|�[�g�̊m�ۂɎ��s���܂���.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			this.socketConnector_ = null;

			return;
		}

		Thread msgRecieveThread = new Thread(this.socketConnector_);
		msgRecieveThread.start();

		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
		this.phoneFrame_.appendText("�O���[�v�z�X�g�ɂȂ�܂���");
		InetAddress ia;
		try {
			ia = InetAddress.getLocalHost();
			String ip = ia.getHostAddress(); // IP�A�h���X
			String hostname = ia.getHostName(); // �z�X�g��
			this.phoneFrame_.appendText("�z�X�g���F" + ip + "(" + hostname + ")");
			this.phoneFrame_.appendText("�|�[�g(�Œ�)�F" + groupHostPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �d�����I�t�ɂ���B
	 * 
	 */
	public synchronized void terminatePhoneServer() {
		// ���ɃT�[�o���I�����Ă���ꍇ�A�������Ȃ�
		if (this.socketConnector_ == null) {
			return;
		}

		this.closeConnectedSocketStream();

		this.socketConnector_.terminate();
		this.socketConnector_ = null;
		this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
	}

	/**
	 * �\�P�b�g����M�������ɌĂяo����郁�\�b�h�B
	 * 
	 * @param socket
	 *            �X�g���[�������Ώۂ̃\�P�b�g
	 * @return �ڑ�����
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

			// �\�P�b�g��M���ݒ�
			this.socketConnector_.setRecievedNow(true);

			Thread recieveThread = new Thread(msgReciever);
			recieveThread.start();

			// ��ʕ`��
			this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);

			// �O���[�v���[�h
			if (this.phoneGroupMode_) {
				// ����ɐڑ��������������b�Z�[�W�𑗐M
				String message;
				try {
					message = InetAddress.getLocalHost().getHostName()
							+ "�ɐڑ����܂���.";
					for (MessageSender msgSenderMemb : this.msgSenderList_) {
						msgSenderMemb.sendMessage(message);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				return true;
			}

			// ����ɐڑ��������������b�Z�[�W�𑗐M
			String message = "�ڑ����܂���.";
			msgSender.sendMessage(message);

			return true;
		} catch (IOException ioExc) {
			this.showErrorMessage("�X�g���[���̎擾�Ɏ��s���܂���.");
			this.msgRecieverList_.add(null);
			this.msgSenderList_.add(null);
			return false;
		}
	}

	/**
	 * ���g�ѓd�b�A�v���ɐڑ�����B
	 * 
	 */
	public synchronized void startConnection() {
		String portStr = this.phoneFrame_.getConnectPortStr();
		int portNumber = this.getPortNumberFromStr(portStr);

		if (portNumber == INVALID_PORT_NUMBER) {
			String errMsg = "�ڑ���|�[�g�ԍ��� " + MINMUM_PORT_NUMBER + " �` "
					+ MAXMUM_PORT_NUMBER + " �̐����l���w�肵�Ă�������.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			return;
		}

		String hostStr = this.phoneFrame_.getConnectHostStr();
		InetAddress address = null;

		try {
			address = InetAddress.getByName(hostStr);
		} catch (UnknownHostException uhExc) {
			this.showErrorMessage("�ڑ��悪������܂���.");
			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			return;
		}

		Socket connectSocket = this.connectServer(address, portNumber);

		// �ڑ������Ɏ��s�����ꍇ
		if (connectSocket == null) {
			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			return;
		}

		boolean isAccepted = this.acceptSocket(connectSocket);

		// �X�g���[���̎擾�Ɏ��s�����ꍇ
		if (isAccepted == false) {
			this.errorLog("��M�����\�P�b�g��j�����܂�.");
			String errMsg = "�X�g���[���擾���Ɉُ킪���������ׁA�ڑ��𒆒f���܂�.";
			this.cancelNewConnection(connectSocket, errMsg);

			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			return;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);
	}

	/**
	 * ���g�ѓd�b�A�v���Ƃ̐ڑ�����������B
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

		// �ʏ�I���t���O������������
		this.setNormalDisconnect_(false);
		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
	}

	/**
	 * ���g�ѓd�b�A�v���Ƃ̐ڑ�����������B
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

		// �ʏ�I���t���O������������
		this.setNormalDisconnect_(false);
		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
	}

	/**
	 * ���b�Z�[�W�𑗐M����B
	 * 
	 */
	public synchronized void sendMessage() {
		String message = this.phoneFrame_.getSendMessage();

		// �󕶎���̏ꍇ�͑��M���Ȃ�
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
	 * ���b�Z�[�W����M����B
	 * 
	 * @param message
	 *            ��M���b�Z�[�W
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
	 * �G���[���b�Z�[�W�\���������s���B
	 * 
	 * @param errMsg
	 */
	public synchronized void showErrorMessage(String errMsg) {
		this.phoneFrame_.showErrorDialog(errMsg);
	}

	/**
	 * �G���[���O���o�͂���B
	 * 
	 * @param errMsg
	 *            �G���[���b�Z�[�W
	 */
	public synchronized void errorLog(String errMsg) {
		System.err.println(errMsg);
	}

	/**
	 * ��M�����\�P�b�g�Ƃ̐ڑ���j������B
	 * 
	 * @param socket
	 *            �ڑ���j������\�P�b�g
	 * @param sendMsg
	 *            ��M�����\�P�b�g�ɑ��M���郁�b�Z�[�W
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
			// ����ɐڑ��s�\�ł��邱�Ƃ𑗐M�ł��Ȃ�����
			this.showErrorMessage("�ڑ��I�����b�Z�[�W���M���s.");
		} finally {
			if (buffWriter != null) {
				try {
					buffWriter.close();
				} catch (IOException ioExc) {
					// �������Ȃ�
					ioExc.printStackTrace();
				}
			}
			try {
				socket.close();
			} catch (IOException ioExc) {
				// �������Ȃ�
				ioExc.printStackTrace();
			}
		}
	}

	/**
	 * �|�[�g�ԍ������񂩂琔�l���擾����B
	 * 
	 * �w�肳�ꂽ�����񂪐��l�łȂ��A�܂��̓|�[�g�ԍ��Ƃ��ĕs�K�؂ȏꍇ�� �s���l�ł��邱�Ƃ������l(INVALID_PORT_NUMBER)��Ԃ��B
	 * 
	 * @param portStr
	 *            �|�[�g�ԍ�������
	 * @return �|�[�g�ԍ�
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
			System.err.println("���l�ւ̕ϊ��Ɏ��s");
		}

		return portNumber;
	}

	/**
	 * �T�[�o�ɐڑ��������s���B �ڑ��Ɏ��s�����ꍇ��null��Ԃ��B
	 * 
	 * @param address
	 *            �ڑ���z�X�g
	 * @param portNumber
	 *            �ڑ���|�[�g
	 * @return �ڑ��ɐ��������\�P�b�g
	 */
	private Socket connectServer(InetAddress address, int portNumber) {
		Socket connectSocket = new Socket();

		try {
			// �ڑ���z�X�g�ƃ|�[�g���w�肵�ăT�[�o�ɐڑ�����
			InetSocketAddress socketAddress = new InetSocketAddress(address,
					portNumber);
			connectSocket.connect(socketAddress,
					PhoneConstant.TIMEOUT_SOCKET_CONNECT);
		} catch (SocketTimeoutException timeoutExc) {
			this.showErrorMessage("�^�C���A�E�g�ɂ��ڑ��ł��܂���ł���.");
			connectSocket = null;
		} catch (IOException ioExc) {
			this.showErrorMessage("�ڑ��Ɏ��s���܂���.");
			connectSocket = null;
		}

		return connectSocket;
	}

	/**
	 * ���ݎ�M���̃\�P�b�g�̐ڑ������B
	 * 
	 */
	private void closeConnectedSocketStream() {
		if (this.msgSenderList_ != null) {
			for (MessageSender msgSender : this.msgSenderList_) {
				msgSender.sendMessage("�ڑ����������܂���.");
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
				// �������Ȃ�
			} finally {
				this.clientReciverAdressMap_.clear();
			}
		}
	}

	/**
	 * ���ݎ�M���̃\�P�b�g�̐ڑ������B
	 * 
	 */
	private void closeConnectedSocketStream(MessageReciever msgReciever) {
		int numbOfRecandSen = this.msgRecieverList_.indexOf(msgReciever);
		if (this.msgSenderList_.get(numbOfRecandSen) != null) {
			MessageSender msgSender = this.msgSenderList_.get(numbOfRecandSen);
			msgSender.sendMessage("�ڑ����������܂���.");
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
				// �������Ȃ�
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
				String errMsg = "���|�[�g�ԍ��� " + MINMUM_PORT_NUMBER + " �` "
						+ MAXMUM_PORT_NUMBER + " �̐����l���w�肵�Ă�������.";
				this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
				this.showErrorMessage(errMsg);
			} else {
				this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			}
		}
	}

}
