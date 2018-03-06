package sample.samplePhone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;

/**
 * �g�ѓd�b�A�v���̃A�N�V�������X�i�B
 */
public class PhoneListener implements ActionListener, KeyListener, ItemListener {
	/** �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g�B */
	private PhoneController controller_;

	/** �A���[�g���b�Z�[�W�B */
	String lightImageText;

	public void setAlertMessage(String lightImageText) {
		this.lightImageText = lightImageText;
	}

	/**
	 * �R���X�g���N�^�B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ��I�u�W�F�N�g
	 */
	public PhoneListener(PhoneController controller) {
		// �t�B�[���h�̏�����
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
		if ((PhoneConstant.CMD_POWERED_BUTTON.equals(cmd) == true) && (isPushed == true)) {
			this.controller_.startPhoneServer();
		}
		// �d���{�^�����������A�d�����I�t�ɂ����ꍇ
		else if ((PhoneConstant.CMD_POWERED_BUTTON.equals(cmd) == true) && (isPushed == false)) {
			this.controller_.setNormalDisconnect_(true);
			this.controller_.terminatePhoneServer();
		}
		// �ڑ��{�^�����������A�ڑ�����ԂɈڍs�����ꍇ
		else if ((PhoneConstant.CMD_CONNECT_SERVER_BUTTON.equals(cmd) == true) && (isPushed == true)) {
			this.controller_.startConnection();
		}
		// �ڑ��{�^�����������A�ڑ�����Ԃ����������ꍇ
		else if ((PhoneConstant.CMD_CONNECT_SERVER_BUTTON.equals(cmd) == true) && (isPushed == false)) {
			this.controller_.setNormalDisconnect_(true);
			this.controller_.terminateConnection();
		}
		// ���M�{�^�������������ꍇ
		else if (PhoneConstant.CMD_SEND_MESSAGE_BUTTON.equals(cmd) == true) {
			this.controller_.sendMessage();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.controller_.sendMessage();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		JCheckBox checkbox = (JCheckBox) e.getItem();
		String boxLabel = checkbox.getText();
		if (boxLabel.equals("P2P Mode")) {

			if (e.getStateChange() == 1) {
				this.controller_.setPhoneP2PMode_(PhoneConstant.NON_INTERRUPT_MODE);
				this.controller_.toggleConnentionStatus();
			} else if (e.getStateChange() == 2) {
				this.controller_.setPhoneP2PMode_(!PhoneConstant.NON_INTERRUPT_MODE);
				this.controller_.toggleConnentionStatus();
			} else {

			}
		} else if (boxLabel.equals("Group Host Mode")) {
			if (e.getStateChange() == 1) {
				// groug talk
				this.controller_.setNormalDisconnect_(true);
				this.controller_.terminatePhoneServer();
				this.controller_.setPhoneGroupMode_(true);
				this.controller_.setPhoneP2PMode_(false);
				this.controller_.startPhoneServer(7372); // Group Host Port
															// Fixed Value:7372
				this.controller_.toggleConnentionStatus();
			} else if (e.getStateChange() == 2) {
				this.controller_.setNormalDisconnect_(true);
				this.controller_.terminatePhoneServer();
				this.controller_.setPhoneGroupMode_(false);
				this.controller_.startPhoneServer();
				this.controller_.toggleConnentionStatus();
			} else {

			}
		}
	}

}
