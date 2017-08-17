package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

/**
 * 定数を定義するインタフェース。
 * 
 * @author Acroquest
 * 
 */
public interface PhoneConstant {
	/** 電源ボタン押下を示すアクションコマンド。 */
	public static final String CMD_POWERED_BUTTON = "cmdPoweredButton";

	/** 接続ボタン押下を示すアクションコマンド。 */
	public static final String CMD_CONNECT_SERVER_BUTTON = "cmdConnectServerButton";

	/** 送信ボタン押下を示すアクションコマンド。 */
	public static final String CMD_SEND_MESSAGE_BUTTON = "cmdSendMessageButton";

	/** ORBに設定するホスト名のキー。 */
	public static final String ORB_HOST_KEY = "org.omg.CORBA.ORBInitialHost";

	/** ORBに設定するポート番号のキー。 */
	public static final String ORB_PORT_KEY = "org.omg.CORBA.ORBInitialPort";

	/** RootPOA. */
	public static final String ORB_ROOT_POA = "RootPOA";

	/** Name Service */
	public static final String ORB_NAME_SERVICE = "NameService";
}
