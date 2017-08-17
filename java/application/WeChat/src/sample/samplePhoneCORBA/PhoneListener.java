package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

/**
 * �g�ѓd�b�A�v���̃A�N�V�������X�i�B
 * 
 * @author Acroquest
 * 
 */
public class PhoneListener implements ActionListener {
	/** �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g�B */
	private PhoneController controller_;

	/**
	 * �R���X�g���N�^�B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 */
	public PhoneListener(PhoneController controller) {
		this.controller_ = controller;
	}

	/**
	 * �g�ѓd�b�A�v���̃{�^�����������ꂽ�Ƃ��ɌĂ΂��B
	 * 
	 * @param actEvt
	 *            �A�N�V�����C�x���g
	 * @see java.awt.event.ActionListener
	 *      #actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent actEvt) {
		String cmd = actEvt.getActionCommand();
		Object tmpSource = actEvt.getSource();

		if ((tmpSource instanceof AbstractButton) == false) {
			// �ُ폈��
			System.err.println("�I�����ꂽ�R���|�[�l���g���z��O.");
			return;
		}

		AbstractButton buttonSource = (AbstractButton) tmpSource;
		boolean isPushed = buttonSource.isSelected();

		// �d���{�^�����������A�d�����I���ɂ����ꍇ
		if ((PhoneConstant.CMD_POWERED_BUTTON.equals(cmd) == true)
				&& (isPushed == true)) {
			this.controller_.startPhoneServer();
		}
		// �d���{�^�����������A�d�����I�t�ɂ����ꍇ
		else if ((PhoneConstant.CMD_POWERED_BUTTON.equals(cmd) == true)
				&& (isPushed == false)) {
			this.controller_.terminateServer();
		}
		// �ڑ��{�^�����������A�ڑ�����ԂɈڍs�����ꍇ
		else if ((PhoneConstant.CMD_CONNECT_SERVER_BUTTON.equals(cmd) == true)
				&& (isPushed == true)) {
			this.controller_.startConnection();
		}
		// �ڑ��{�^�����������A�ڑ�����Ԃ����������ꍇ
		else if ((PhoneConstant.CMD_CONNECT_SERVER_BUTTON.equals(cmd) == true)
				&& (isPushed == false)) {
			this.controller_.terminateConnectiton();
		}
		// ���M�{�^�������������ꍇ
		else if (PhoneConstant.CMD_SEND_MESSAGE_BUTTON.equals(cmd) == true) {
			this.controller_.sendMessage();
		}
	}
}
