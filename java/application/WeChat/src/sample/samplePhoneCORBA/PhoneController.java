package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.awt.Dimension;
import java.util.Properties;

import org.omg.CORBA.ORB;

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
	private static final int MINMUM_PORT_NUMBER = 900;

	/** �|�[�g�ԍ��Ƃ��Đݒ�ł���l�̍ő�l�B */
	private static final int MAXMUM_PORT_NUMBER = 65535;

	/** �������������s��ꂽ�����f����t���O�B */
	private boolean isInitialized_;

	/** ORB. */
	private org.omg.CORBA.ORB orb_;

	/** �g�ѓd�b����̐ڑ�����M����T�[�o�B */
	private PhoneServer phoneServer_;

	/** ���ݒʐM���̌g�ѓd�b�̖��O�B */
	private String otherPhoneName_;

	/** �g�ѓd�b�E�B���h�E�B */
	private PhoneFrame phoneFrame_;

	/**
	 * �R���X�g���N�^�B �R���X�g���N�^���s��A���������\�b�hinitialize()�����s����K�v������B
	 * 
	 */
	public PhoneController() {
		this.phoneFrame_ = new PhoneFrame(this);
		this.isInitialized_ = false;
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

	/**
	 * �d�����I���ɂ��A�ڑ���M�������J�n����B
	 * 
	 */
	public void startPhoneServer() {
		String ownName = this.phoneFrame_.getOwnName();

		if ((ownName == null) || ("".equals(ownName.trim()) == true)) {
			String errMsg = "�����̖��O����͂��ĉ�����.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);

			return;
		}

		String namingServiceHost = this.phoneFrame_.getNamingServiceHost();
		if (this.checkName(namingServiceHost) == false) {
			String errMsg = "�l�[�~���O�T�[�r�X�̃z�X�g����͂��Ă�������.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);

			return;
		}

		String namingServicePortStr = this.phoneFrame_
				.getNamingServicePortStr();
		int namingServicePortNumber = this
				.getPortNumberFromStr(namingServicePortStr);

		if (namingServicePortNumber == INVALID_PORT_NUMBER) {
			String errMsg = "�|�[�g�ԍ��� " + MINMUM_PORT_NUMBER + " �` "
					+ MAXMUM_PORT_NUMBER + " �̐����l���w�肵�Ă�������.";
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
			String errMsg = "ORB�ւ̐ڑ��Ɏ��s���܂���.";
			this.showErrorMessage(errMsg);
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			this.phoneServer_ = null;

			coExc.printStackTrace();
			return;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
		this.phoneFrame_.appendText("�d�����I���ɂ��܂���.");
	}

	/**
	 * �d�����I�t�ɂ���B
	 * 
	 */
	public synchronized void terminateServer() {
		// �T�[�o���N�����Ă��Ȃ��ꍇ
		if (this.phoneServer_ == null) {
			this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
			return;
		}

		String ownName = this.phoneFrame_.getOwnName();
		try {
			this.phoneServer_.terminateServer(ownName);
		} catch (CorbaOperationException coExc) {
			String errMsg = "�d���I�t�����Ɏ��s���܂���.";
			this.phoneFrame_.appendText(errMsg);
			return;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.POWER_OFF);
	}

	/**
	 * �ڑ��v������M�������ɌĂяo�����B
	 * 
	 * @param senderName
	 *            �@���M�Җ�
	 * @param recieverName
	 *            ��M�Җ�
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

		// ���ɒʐM���̏ꍇ�A�ڑ������ۂ���
		if ((this.otherPhoneName_ != null)
				&& ("".equals(this.otherPhoneName_.trim()) == false)) {
			return false;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);
		this.phoneFrame_.setConnectName(senderName);

		String message = senderName + " ����̐ڑ�����M���܂���.";
		this.phoneFrame_.appendText(message);
		this.otherPhoneName_ = senderName;

		return true;
	}

	/**
	 * �ڑ��Ւf�v������M�����Ƃ��ɌĂяo�����B
	 * 
	 * @param senderName
	 *            ���M�Җ�
	 * @param recieverName
	 *            ��M�Җ�
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

		String message = senderName + " ����ڑ����Ւf����܂���.";
		this.phoneFrame_.appendText(message);
		this.otherPhoneName_ = null;

		return true;
	}

	/**
	 * ���g�ѓd�b�A�v���ɐڑ�����B
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

		// �ڑ������Ɏ��s�����ꍇ
		if (result == false) {
			this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
			String errMsg = otherName + " �ւ̐ڑ��Ɏ��s���܂���.";
			this.phoneFrame_.appendText(errMsg);
			this.otherPhoneName_ = null;

			return;
		}

		this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);
		this.otherPhoneName_ = otherName;

		// ����ɐڑ��������������b�Z�[�W�𑗐M
		String message = otherName + " �ɐڑ����܂���.";
		this.phoneFrame_.appendText(message);
	}

	/**
	 * ���g�ѓd�b�A�v���Ƃ̐ڑ�����������B
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
			String errMsg = "�ڑ��𒆒f�ł��܂���ł���.";
			this.phoneFrame_.appendText(errMsg);

			this.phoneFrame_.setViewByStatus(PhoneStatus.CONNECTION);
			return;
		}

		String message = otherName + " �Ƃ̐ڑ��𒆒f���܂���.";
		this.phoneFrame_.appendText(message);
		this.otherPhoneName_ = null;

		this.phoneFrame_.setViewByStatus(PhoneStatus.NO_CONNECTION);
	}

	/**
	 * ���b�Z�[�W�𑗐M����B
	 * 
	 */
	public synchronized void sendMessage() {
		String message = this.phoneFrame_.getSendMessage();
		boolean isValidMsg = this.checkName(message);

		// �󕶎���̏ꍇ�͑��M���Ȃ�
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
			String errMsg = "���b�Z�[�W�̑��M�Ɏ��s���܂���.";
			this.showErrorMessage(errMsg);
			return;
		}

		this.phoneFrame_.appendText(message);
	}

	/**
	 * ���b�Z�[�W����M����B
	 * 
	 * @param message
	 *            ��M���b�Z�[�W
	 */
	public synchronized boolean recieveMessage(String senderName, String message) {
		String otherName = this.phoneFrame_.getConnectName();
		boolean isValid = this.checkName(otherName);
		boolean isValidArg = this.checkName(senderName);

		if ((isValid == false) || (isValidArg == false)) {
			return false;
		}

		if (otherName.trim().equals(senderName.trim()) == false) {
			String errMsg = "���b�Z�[�W�̑��M�Ɏ��s���܂���.";
			this.showErrorMessage(errMsg);
			return false;
		}

		this.phoneFrame_.appendText(message);

		return true;
	}

	/**
	 * �G���[���b�Z�[�W�\���������s���B
	 * 
	 * @param errMsg
	 *            �G���[���b�Z�[�W
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
			this.errorLog("���l�ւ̕ϊ��Ɏ��s");
		}

		return portNumber;
	}

	/**
	 * ORB������������B
	 * 
	 * @param namingSeviceHost
	 *            �l�[�~���O�T�[�r�X�̃z�X�g
	 * @param namingServicePort
	 *            �l�[�~���O�T�[�r�X�̃|�[�g
	 * @return
	 */
	private ORB initOrb(String namingSeviceHost, String namingServicePort) {
		Properties property = new Properties();
		property.put(PhoneConstant.ORB_HOST_KEY, namingSeviceHost);
		property.put(PhoneConstant.ORB_PORT_KEY, namingServicePort);

		String[] initArgs = null;
		// ORB�̐����Ə��������s��
		ORB orb = ORB.init(initArgs, property);

		return orb;
	}

	/**
	 * ���O���K�؂Ȃ��̂��`�F�b�N����B
	 * 
	 * @param name
	 *            ���O
	 * @return �K�؂Ȃ��true, �s�K�؂Ȃ��false
	 */
	private boolean checkName(String name) {
		if (name == null) {
			return false;
		}

		boolean isValid = !("".equals(name.trim()));

		return isValid;
	}
}
