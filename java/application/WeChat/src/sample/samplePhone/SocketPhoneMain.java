package sample.samplePhone;

import java.awt.Dimension;

/**
 * ソケット版携帯電話アプリを起動するメインクラス。
 */
/** 携帯電話アプリのウィンドウ幅。 */
/** 携帯電話アプリのウィンドウ高さ。 */
/**
 * メインメソッド。
 */
/**
 * ソケット版携帯電話アプリを起動するメインクラス。
 */
public class SocketPhoneMain {
	/** 携帯電話アプリのウィンドウ幅。 */
	private static final int FRAME_WIDTH = 800;

	/** 携帯電話アプリのウィンドウ高さ。 */
	private static final int FRAME_HEIGHT = 560;

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
