package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

/**
 * CORBA�ʐM�̗�O�����b�v����N���X�B
 * 
 * @author Acroquest
 * 
 */
public class CorbaOperationException extends Exception {
	/**
	 * �R���X�g���N�^�B
	 * 
	 * @param throwable
	 */
	public CorbaOperationException(Throwable throwable) {
		super(throwable);
	}
}
