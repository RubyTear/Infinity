package sample.samplePhone;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

/**
 * �萔���`����C���^�t�F�[�X�B
 * 
 * @author Acroquest
 * 
 */
public interface PhoneConstant {
	/** �d���{�^�������������A�N�V�����R�}���h�B */
	public static final String CMD_POWERED_BUTTON = "cmdPoweredButton";

	/** �ڑ��{�^�������������A�N�V�����R�}���h�B */
	public static final String CMD_CONNECT_SERVER_BUTTON = "cmdConnectServerButton";

	/** ���M�{�^�������������A�N�V�����R�}���h�B */
	public static final String CMD_SEND_MESSAGE_BUTTON = "cmdSendMessageButton";

	/** �\�P�b�g��M�̃^�C���A�E�g����(ms) */
	public static final int TIMEOUT_SOCKET_CONNECT = 10000;

	/** �T�[�o�\�P�b�g�̑҂��󂯎���(ms) */
	public static final int TIMEOUT_SERVER_SOCKET = 500;

	/** �󑗐M�G���R�[�h�w�� */
	public static final String ENCODE_UTF_8 = "UTF-8";
	
	/** P2P(point-to-point)���[�h */
	public static final boolean NON_INTERRUPT_MODE = true;
}