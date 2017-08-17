package sample.samplePhoneCORBA;

/******************************************************************************
 * All of this source code are all rights reserved by Acroquest Co., Ltd. .
 ******************************************************************************/

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 * 携帯電話の表示ウィンドウを表す。
 * 
 * @author Acroquest
 * 
 */
public class PhoneFrame extends JFrame {
	/** ウィンドウのタイトル文字列。 */
	private static final String FRAME_TITLE = "HandyPhone (CORBA)";

	/** ウィンドウの最小サイズ。 */
	private static final Dimension FRAME_MINIMUM_SIZE = new Dimension(500, 350);

	/** ボタンのデフォルトサイズ。 */
	private static final Dimension BUTTON_PERFORMED_SIZE = new Dimension(86, 27);

	/** フィールドが初期化されているか判断するフラグ。 */
	private boolean isFieldInitialized_;

	/** 自分の名前を設定するテキストフィールド。 */
	private JTextField ownNameText_;

	/** ネーミングサービスのホストを設定するテキストフィールド。 */
	private JTextField namingServiceHostText_;

	/** ネーミングサービスのポート番号を設定するテキストフィールド。 */
	private JTextField namingServicePortText_;

	/** 接続先の名前を設定するテキストフィールド。 */
	private JTextField connectNameText_;

	/** 送信メッセージを入力するテキストフィールド。 */
	private JTextField messageText_;

	/** 送受信メッセージを表示するテキストエリア。 */
	private JTextArea displayArea_;

	/** ソケット受信待ち状態の設定を行うトグルボタン。 */
	private JToggleButton powerTButton_;

	/** 接続状態の設定を行うトグルボタン。 */
	private JToggleButton connectTButton_;

	/** メッセージを送信するボタン。 */
	private JButton sendButton_;

	/**
	 * コンストラクタ。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理
	 */
	public PhoneFrame(PhoneController controller) {
		// フィールドの初期化
		this.initializeField();
		this.setActionListener(controller);

		JPanel northPanel = this.createNorthPanel(this.ownNameText_,
				this.namingServiceHostText_, this.namingServicePortText_,
				this.powerTButton_);
		JPanel centorPanel = this.createCentorPanel(this.displayArea_);
		JPanel southPanel = this.createSouthPanel(this.connectNameText_,
				this.connectTButton_, this.messageText_, this.sendButton_);
		JPanel phonePanel = this.createPhonePanel(northPanel, centorPanel,
				southPanel);

		Container container = this.getContentPane();
		container.add(phonePanel);
	}

	/**
	 * 表示ウィンドウに文字列を追加する。
	 * 
	 * @param text
	 *            追加する文字列
	 */
	public synchronized void appendText(String text) {
		this.displayArea_.append(text + "\n");
	}

	/**
	 * エラーダイアログを表示する。
	 * 
	 * @param errMsg
	 *            エラーメッセージ
	 */
	public synchronized void showErrorDialog(String errMsg) {
		System.err.println(errMsg);
		this.appendText(errMsg);
	}

	/**
	 * 携帯電話ウィンドウの描画をステータスに合わせて更新する。
	 * 
	 * @param status
	 *            変化後のステータス
	 */
	public void setViewByStatus(PhoneStatus status) {
		boolean isOwnNameTextEditable = false;
		boolean isNamingServiceTextEditable = false;
		boolean isOwnPortTButtonSelected = false;
		boolean isDisplayEnabled = false;
		boolean isConnectionTextEditable = false;
		boolean isConnectionTButtonEnabled = false;
		boolean isConnectionTButtonSelected = false;
		boolean isMessageTextEditable = false;
		boolean isMessageButtonEnabled = false;

		String ownPortLabel = "電源";
		String connectionLabel = "未接続";

		switch (status) {
		case POWER_OFF: // 電源が入っていない状態
		{
			isOwnNameTextEditable = true;
			isNamingServiceTextEditable = true;
			this.displayArea_.setText("");
			this.connectNameText_.setText("");

			break;
		}
		case NO_CONNECTION: // 接続がない状態
		{
			isOwnPortTButtonSelected = true;
			isDisplayEnabled = true;
			isConnectionTextEditable = true;
			isConnectionTButtonEnabled = true;
			this.messageText_.setText("");

			ownPortLabel = "電源OFF";
			connectionLabel = "接続開始";

			break;
		}
		case CONNECTION: // 接続中の状態
		{
			isOwnPortTButtonSelected = true;
			isDisplayEnabled = true;
			isConnectionTButtonEnabled = true;
			isConnectionTButtonSelected = true;
			isMessageTextEditable = true;
			isMessageButtonEnabled = true;

			ownPortLabel = "電源OFF";
			connectionLabel = "接続中断";

			break;
		}

		default: {
			System.err.println("異常なステータス");
			break;
		}
		}

		// 自ポート設定
		this.ownNameText_.setEditable(isOwnNameTextEditable);
		this.namingServiceHostText_.setEditable(isNamingServiceTextEditable);
		this.namingServicePortText_.setEditable(isNamingServiceTextEditable);
		this.powerTButton_.setSelected(isOwnPortTButtonSelected);
		this.powerTButton_.setText(ownPortLabel);

		this.displayArea_.setEnabled(isDisplayEnabled);

		// 接続設定
		this.connectNameText_.setEditable(isConnectionTextEditable);
		this.connectTButton_.setSelected(isConnectionTButtonSelected);
		this.connectTButton_.setEnabled(isConnectionTButtonEnabled);
		this.connectTButton_.setText(connectionLabel);

		// 送信設定
		this.messageText_.setEditable(isMessageTextEditable);
		this.sendButton_.setEnabled(isMessageButtonEnabled);
	}

	/**
	 * 自分の名前として指定された文字列を返す。
	 * 
	 * @return 自分の名前
	 */
	public String getOwnName() {
		String ownName = this.ownNameText_.getText();
		return ownName;
	}

	/**
	 * ネーミングサービスのホストとして指定された文字列を返す。
	 * 
	 * @return ネーミングサービスのホスト入力欄の文字列
	 */
	public String getNamingServiceHost() {
		String host = this.namingServiceHostText_.getText();
		return host;
	}

	/**
	 * ネーミングサービスのポートとして指定された文字列を返す。
	 * 
	 * @return ネーミングサービスのポート入力欄の文字列
	 */
	public String getNamingServicePortStr() {
		String portStr = this.namingServicePortText_.getText();
		return portStr;
	}

	/**
	 * 接続先として指定された名前の文字列を返す。
	 * 
	 * @return 接続先ホストの文字列
	 */
	public String getConnectName() {
		String otherNameStr = this.connectNameText_.getText();
		return otherNameStr;
	}

	/**
	 * 接続先として指定された名前の文字列を設定する。
	 * 
	 * @param connectName
	 *            接続先ホストの文字列
	 */
	public void setConnectName(String connectName) {
		this.connectNameText_.setText(connectName);
	}

	/**
	 * メッセージテキストに入力された文字列を返す。
	 * 
	 * @return 送信メッセージ
	 */
	public String getSendMessage() {
		String message = this.messageText_.getText();
		return message;
	}

	/**
	 * フィールドの初期化処理を行う。 このメソッドは原則一度しか呼び出されない。
	 * 
	 */
	private void initializeField() {
		if (this.isFieldInitialized_ == true) {
			return;
		}

		this.isFieldInitialized_ = true;

		this.ownNameText_ = new JTextField();
		this.ownNameText_.setColumns(15);
		this.namingServiceHostText_ = new JTextField();
		this.namingServiceHostText_.setColumns(15);
		this.namingServiceHostText_.setText("localhost");
		this.namingServicePortText_ = new JTextField();
		this.namingServicePortText_.setColumns(6);
		this.namingServicePortText_.setText("900");
		this.connectNameText_ = new JTextField();
		this.connectNameText_.setColumns(15);
		this.messageText_ = new JTextField();
		this.messageText_.setColumns(29);
		this.displayArea_ = new JTextArea();
		this.displayArea_.setEditable(false);

		this.powerTButton_ = new JToggleButton("電源ON");
		this.connectTButton_ = new JToggleButton("未接続");
		this.sendButton_ = new JButton("送信");

		this.powerTButton_.setPreferredSize(BUTTON_PERFORMED_SIZE);
		this.connectTButton_.setPreferredSize(BUTTON_PERFORMED_SIZE);
		this.sendButton_.setPreferredSize(BUTTON_PERFORMED_SIZE);

		this.powerTButton_.setActionCommand(PhoneConstant.CMD_POWERED_BUTTON);
		this.connectTButton_
				.setActionCommand(PhoneConstant.CMD_CONNECT_SERVER_BUTTON);
		this.sendButton_
				.setActionCommand(PhoneConstant.CMD_SEND_MESSAGE_BUTTON);

		this.setViewByStatus(PhoneStatus.POWER_OFF);

		this.setTitle(FRAME_TITLE);
		this.setMinimumSize(FRAME_MINIMUM_SIZE);
	}

	/**
	 * アクションリスナを設定する。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理
	 */
	private void setActionListener(PhoneController controller) {
		ActionListener listener = new PhoneListener(controller);
		this.powerTButton_.addActionListener(listener);
		this.connectTButton_.addActionListener(listener);
		this.sendButton_.addActionListener(listener);
	}

	/**
	 * 携帯電話ウィンドウの初期設定を入力するパネルを作成する。
	 * 
	 * @param nameText
	 *            自分の名前を入力するフィールド
	 * @param hostText
	 *            ネーミングサービスのホスト入力フィールド
	 * @param portText
	 *            ネーミングサービスのポート番号入力フィールド
	 * @param powerButton
	 *            接続待ち設定ボタン
	 * @return 自ポート番号入力パネル
	 */
	private JPanel createNorthPanel(JTextField nameText, JTextField hostText,
			JTextField portText, AbstractButton powerButton) {
		JPanel returnPanel = new JPanel();
		BorderLayout borderLayout = new BorderLayout();
		returnPanel.setLayout(borderLayout);
		JLabel hostLabel = new JLabel("HOST");
		JLabel portLabel = new JLabel("PORT");
		JLabel nameLabel = new JLabel("名前");

		FlowLayout rightFlowLayout = new FlowLayout();
		rightFlowLayout.setAlignment(FlowLayout.RIGHT);
		FlowLayout centerFlowLayout = new FlowLayout();
		centerFlowLayout.setAlignment(FlowLayout.CENTER);

		JPanel ownNamePanel = new JPanel();
		ownNamePanel.setLayout(rightFlowLayout);
		ownNamePanel.add(nameLabel);
		ownNamePanel.add(this.ownNameText_);

		JPanel namingServicePanel = new JPanel();
		namingServicePanel.setLayout(rightFlowLayout);
		namingServicePanel.add(hostLabel);
		namingServicePanel.add(this.namingServiceHostText_);
		namingServicePanel.add(portLabel);
		namingServicePanel.add(this.namingServicePortText_);

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(2, 1));
		textPanel.add(ownNamePanel);
		textPanel.add(namingServicePanel);

		JPanel powerPanel = new JPanel();
		powerPanel.setLayout(centerFlowLayout);
		powerPanel.add(powerButton);

		returnPanel.add(textPanel, BorderLayout.CENTER);
		returnPanel.add(powerPanel, BorderLayout.EAST);

		return returnPanel;
	}

	/**
	 * 携帯電話ウィンドウの表示画面付きパネルを作成する。
	 * 
	 * @param displayArea
	 *            表示画面
	 * @return 表示画面付きパネル
	 */
	private JPanel createCentorPanel(JTextArea displayArea) {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(displayArea);
		returnPanel.add(scrollPane, BorderLayout.CENTER);

		return returnPanel;
	}

	/**
	 * 携帯電話ウィンドウの送信設定パネルを作成する。
	 * 
	 * @param otherNameText
	 *            送信先名前を入力するテキストフィールド
	 * @param connectButton
	 *            接続ボタン
	 * @param messageText
	 *            送信メッセージを入力するテキストフィールド
	 * @param sendButton
	 *            送信ボタン
	 * @return 送信設定パネル
	 */
	private JPanel createSouthPanel(JTextField otherNameText,
			AbstractButton connectButton, JTextField messageText,
			AbstractButton sendButton) {
		// 送信先設定パネルの生成
		JPanel connectPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		connectPanel.setLayout(flowLayout);

		JLabel hostLabel = new JLabel("接続先");

		connectPanel.add(hostLabel);
		connectPanel.add(otherNameText);
		connectPanel.add(connectButton);

		// 送信パネルの生成
		JPanel sendPanel = new JPanel();
		sendPanel.setLayout(new FlowLayout());

		JLabel sendLabel = new JLabel("メッセージ");

		sendPanel.add(sendLabel);
		sendPanel.add(messageText);
		sendPanel.add(sendButton);

		// 送信設定パネルの生成
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new GridLayout(2, 1));
		returnPanel.add(connectPanel);
		returnPanel.add(sendPanel);

		return returnPanel;
	}

	/**
	 * 携帯電話ウィンドウのメインパネルを生成する。
	 * 
	 * @param north
	 *            上側に表示するパネル
	 * @param centor
	 *            中央に表示するパネル
	 * @param south
	 *            下側に表示するパネル
	 * @return 携帯電話ウィンドウ
	 */
	private JPanel createPhonePanel(JPanel north, JPanel centor, JPanel south) {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new BorderLayout());

		if (north != null) {
			returnPanel.add(north, BorderLayout.NORTH);
		}

		if (centor != null) {
			returnPanel.add(centor, BorderLayout.CENTER);
		}

		if (south != null) {
			returnPanel.add(south, BorderLayout.SOUTH);
		}

		return returnPanel;
	}
}
