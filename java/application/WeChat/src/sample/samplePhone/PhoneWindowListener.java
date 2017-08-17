package sample.samplePhone;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * �g�ѓd�b�A�v���̃E�B���h�E���X�i�B
 * 
 * @author Acroquest
 * 
 */
public class PhoneWindowListener extends WindowAdapter {
	/** �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g�B */
	private PhoneController controller_;

	/**
	 * �R���X�g���N�^�B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 */
	public PhoneWindowListener(PhoneController controller) {
		// �t�B�[���h�̏�����
		this.controller_ = controller;
	}

	/**
	 * �E�B���h�E�̃N���[�Y�{�^���������������ɌĂ΂��B
	 * 
	 * @param winEvt
	 *            �E�B���h�E�C�x���g
	 * @see java.awt.event.WindowAdapter
	 *      #windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent winEvt) {
		this.controller_.terminatePhoneServer();

		System.exit(0);
	}
}
