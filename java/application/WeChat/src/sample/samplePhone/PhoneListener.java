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
 * 携帯電話アプリのアクションリスナ。
 */
public class PhoneListener implements ActionListener, KeyListener, ItemListener {
	/** 携帯電話アプリ管理オブジェクト。 */
	private PhoneController controller_;

	/** アラートメッセージ。 */
	String lightImageText;

	public void setAlertMessage(String lightImageText) {
		this.lightImageText = lightImageText;
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 */
	public PhoneListener(PhoneController controller) {
		// フィールドの初期化
		this.controller_ = controller;
	}

	/**
	 * 携帯電話アプリのボタンを押下されたときに呼ばれる。
	 * 
	 * @param actEvt
	 *            アクションイベント
	 * @see java.awt.event.ActionListener
	 *      #actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent actEvt) {
		String cmd = actEvt.getActionCommand();
		Object tmpSource = actEvt.getSource();

		if ((tmpSource instanceof AbstractButton) == false) {
			// 異常処理
			System.err.println("選択されたコンポーネントが想定外.");
			return;
		}

		AbstractButton buttonSource = (AbstractButton) tmpSource;
		boolean isPushed = buttonSource.isSelected();

		// 電源ボタンを押下し、電源をオンにした場合
		if ((PhoneConstant.CMD_POWERED_BUTTON.equals(cmd) == true) && (isPushed == true)) {
			this.controller_.startPhoneServer();
		}
		// 電源ボタンを押下し、電源をオフにした場合
		else if ((PhoneConstant.CMD_POWERED_BUTTON.equals(cmd) == true) && (isPushed == false)) {
			this.controller_.setNormalDisconnect_(true);
			this.controller_.terminatePhoneServer();
		}
		// 接続ボタンを押下し、接続中状態に移行した場合
		else if ((PhoneConstant.CMD_CONNECT_SERVER_BUTTON.equals(cmd) == true) && (isPushed == true)) {
			this.controller_.startConnection();
		}
		// 接続ボタンを押下し、接続中状態を解除した場合
		else if ((PhoneConstant.CMD_CONNECT_SERVER_BUTTON.equals(cmd) == true) && (isPushed == false)) {
			this.controller_.setNormalDisconnect_(true);
			this.controller_.terminateConnection();
		}
		// 送信ボタンを押下した場合
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
