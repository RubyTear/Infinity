package sample.samplePhone;

import java.awt.Dimension;

/**
 * �\�P�b�g�Ōg�ѓd�b�A�v�����N�����郁�C���N���X�B
 */
/** �g�ѓd�b�A�v���̃E�B���h�E���B */
/** �g�ѓd�b�A�v���̃E�B���h�E�����B */
/**
 * ���C�����\�b�h�B
 */
/**
 * �\�P�b�g�Ōg�ѓd�b�A�v�����N�����郁�C���N���X�B
 */
public class SocketPhoneMain {
	/** �g�ѓd�b�A�v���̃E�B���h�E���B */
	private static final int FRAME_WIDTH = 800;

	/** �g�ѓd�b�A�v���̃E�B���h�E�����B */
	private static final int FRAME_HEIGHT = 560;

	/**
	 * ���C�����\�b�h�B
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PhoneController controller = new PhoneController();
		Dimension frameSize = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
		controller.initialize(frameSize);
	}
}
