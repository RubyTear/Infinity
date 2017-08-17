package sample.samplePhone;

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

	/** ソケット受信のタイムアウト時間(ms) */
	public static final int TIMEOUT_SOCKET_CONNECT = 10000;

	/** サーバソケットの待ち受け時間(ms) */
	public static final int TIMEOUT_SERVER_SOCKET = 500;

	/** 受送信エンコード指定 */
	public static final String ENCODE_UTF_8 = "UTF-8";
	
	/** P2P(point-to-point)モード */
	public static final boolean NON_INTERRUPT_MODE = true;
}