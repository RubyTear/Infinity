package sample.samplePhone;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 携帯電話アプリのウィンドウリスナ。
 */
public class PhoneWindowListener extends WindowAdapter {
	/** 携帯電話アプリ管理オブジェクト。 */
	private PhoneController controller_;

	/**
	 * コンストラクタ。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理オブジェクト
	 */
	public PhoneWindowListener(PhoneController controller) {
		// フィールドの初期化
		this.controller_ = controller;
	}

	/**
	 * ウィンドウのクローズボタンを押下した時に呼ばれる。
	 * 
	 * @param winEvt
	 *            ウィンドウイベント
	 * @see java.awt.event.WindowAdapter
	 *      #windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent winEvt) {
		this.controller_.terminatePhoneServer();

		System.exit(0);
	}
}
