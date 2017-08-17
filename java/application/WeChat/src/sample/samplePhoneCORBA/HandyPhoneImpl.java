package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

/**
 * �g�ѓd�b�A�v���N���C�A���g����̗v�����T�[�o�Ŏ��s����X�P���g���N���X�B
 * 
 * @author Acroquest
 * 
 */
public class HandyPhoneImpl extends HandyPhoneIFPOA {
	/** �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g�B */
	private PhoneController controller_;

	/**
	 * �R���X�g���N�^�B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 */
	public HandyPhoneImpl(PhoneController controller) {
		this.controller_ = controller;
	}

	/**
	 * �N���C�A���g���ł́A���̃��\�b�h�̓��b�Z�[�W����M�������ɌĂяo�����B
	 * 
	 * @param senderName
	 *            ���M�Җ�
	 * @param message
	 *            ���M���b�Z�[�W
	 * @return ���b�Z�[�W�̎�M�ɐ��������ꍇ��true, ���s�����ꍇ��false
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
	 * �N���C�A���g���ł́A���̃��\�b�h�͐ڑ��v������M�������ɌĂяo�����B
	 * 
	 * @param senderName
	 *            ���M�Җ�
	 * @param recieverName
	 *            ��M�Җ�
	 * @return �ڑ��ɐ��������ꍇ��true, ���s�����ꍇ��false
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
	 * �N���C�A���g�ł́A���̃��\�b�h�͐ڑ��Ւf�v������M�������ɌĂяo�����B
	 * 
	 * @param senderName
	 *            ���M�Җ�
	 * @return �ڑ��Ւf�ɐ��������ꍇ��true, ���s�����ꍇ��false
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
