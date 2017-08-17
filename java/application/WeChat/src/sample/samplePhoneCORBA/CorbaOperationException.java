package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

/**
 * CORBA通信の例外をラップするクラス。
 * 
 * @author Acroquest
 * 
 */
public class CorbaOperationException extends Exception {
	/**
	 * コンストラクタ。
	 * 
	 * @param throwable
	 */
	public CorbaOperationException(Throwable throwable) {
		super(throwable);
	}
}
