package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.awt.Dimension;

/**
 * ソケット版携帯電話アプリを起動するメインクラス。
 * 
 * @author Acroquest
 * 
 */
public class CorbaPhoneMain {
	/** 携帯電話アプリのウィンドウ幅。 */
	private static final int FRAME_WIDTH = 500;

	/** 携帯電話アプリのウィンドウ高さ。 */
	private static final int FRAME_HEIGHT = 350;

	/**
	 * メインメソッド。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PhoneController controller = new PhoneController();
		Dimension frameSize = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
		controller.initialize(frameSize);
	}
}
