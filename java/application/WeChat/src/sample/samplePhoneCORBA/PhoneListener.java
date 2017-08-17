package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

/**
 * 携帯電話アプリのアクションリスナ。
 * 
 * @author Acroquest
 * 
 */
public class PhoneListener implements ActionListener {
	/** 携帯電話アプリ管理オブジェクト。 */
	private PhoneController controller_;

	/**
	 * コンストラクタ。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 */
	public PhoneListener(PhoneController controller) {
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
		if ((PhoneConstant.CMD_POWERED_BUTTON.equals(cmd) == true)
				&& (isPushed == true)) {
			this.controller_.startPhoneServer();
		}
		// 電源ボタンを押下し、電源をオフにした場合
		else if ((PhoneConstant.CMD_POWERED_BUTTON.equals(cmd) == true)
				&& (isPushed == false)) {
			this.controller_.terminateServer();
		}
		// 接続ボタンを押下し、接続中状態に移行した場合
		else if ((PhoneConstant.CMD_CONNECT_SERVER_BUTTON.equals(cmd) == true)
				&& (isPushed == true)) {
			this.controller_.startConnection();
		}
		// 接続ボタンを押下し、接続中状態を解除した場合
		else if ((PhoneConstant.CMD_CONNECT_SERVER_BUTTON.equals(cmd) == true)
				&& (isPushed == false)) {
			this.controller_.terminateConnectiton();
		}
		// 送信ボタンを押下した場合
		else if (PhoneConstant.CMD_SEND_MESSAGE_BUTTON.equals(cmd) == true) {
			this.controller_.sendMessage();
		}
	}
}
