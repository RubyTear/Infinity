package sample.samplePhone;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.awt.Dimension;

/**
 * �\�P�b�g�Ōg�ѓd�b�A�v�����N�����郁�C���N���X�B
 *
 * @author Acroquest
 *
 */
/** �g�ѓd�b�A�v���̃E�B���h�E���B */
/** �g�ѓd�b�A�v���̃E�B���h�E�����B */
/**
 * ���C�����\�b�h�B
 *
 * @param args
 */
/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/
/**
 * �\�P�b�g�Ōg�ѓd�b�A�v�����N�����郁�C���N���X�B
 * 
 * @author Acroquest
 * 
 */
public class SocketPhoneMain {
	/** �g�ѓd�b�A�v���̃E�B���h�E���B */
	private static final int FRAME_WIDTH = 500;

	/** �g�ѓd�b�A�v���̃E�B���h�E�����B */
	private static final int FRAME_HEIGHT = 350;

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
