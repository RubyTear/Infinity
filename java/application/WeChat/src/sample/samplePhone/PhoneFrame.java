package sample.samplePhone;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.UnsupportedEncodingException;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 携帯電話の表示ウィンドウを表す。
 * 
 */
public class PhoneFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** ウィンドウのタイトル文字列。 */
	private static final String FRAME_TITLE = "WeChat (Socket)";

	/** ウィンドウの最小サイズ。 */
	private static final Dimension FRAME_MINIMUM_SIZE = new Dimension(500, 350);

	/** ボタンのデフォルトサイズ。 */
	private static final Dimension BUTTON_PERFORMED_SIZE = new Dimension(86, 27);

	/** フィールドが初期化されているか判断するフラグ。 */
	private boolean isFieldInitialized_;

	/** 自ポート番号を設定するテキストフィールド。 */
	private JTextField ownPortText_;

	/** 接続先のホストを設定するテキストフィールド。 */
	private JTextField connectHostText_;

	/** 接続先のポート番号を設定するテキストフィールド。 */
	private JTextField connectPortText_;

	/** 送信メッセージを入力するテキストフィールド。 */
	private JTextField messageText_;

	/** 送受信メッセージを表示するテキストエリア。 */
	private JTextArea displayArea_;

	/** 送受信メッセージのScrollPane。 */
	private JScrollPane distplayScrollPane_;

	/** ソケット受信待ち状態の設定を行うトグルボタン。 */
	private JToggleButton powerTButton_;

	/** 接続状態の設定を行うトグルボタン。 */
	private JToggleButton connectTButton_;

	/** メッセージを送信するボタン。 */
	private JButton sendButton_;

	/** メッセージを送信するボタン。 */
	private JCheckBox setP2PBox_;

	/** メッセージを送信するボタン。 */
	private JCheckBox asGroupHost_;

	/** iconImage取得。 */
	// ImageIcon ICON = new ImageIcon("./icon/wechat2.png");
	Image ICON = new ImageIcon(this.getClass().getClassLoader().getResource("wechat2.png")).getImage();

	private JFrame frame;

	private SystemTray tray = SystemTray.getSystemTray();

	private TrayIcon trayIcon;

	private PopupMenu defaultMenu;

	private static Object INSTANCE_LOCK = new Object();

	private Image imgLight;

	private PhoneController phoneController_;

	/**
	 * コンストラクタ。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理
	 */
	public PhoneFrame(PhoneController controller) {

		this.phoneController_ = controller;

		// フィールドの初期化
		this.initializeField();

		// img = new
		// ImageIcon(TaskTrayUtil.class.getResource("../tray_icon.gif"))
		// .getImage();
		// img = this.ICON.getImage();

		imgLight = new ImageIcon(this.getClass().getClassLoader().getResource("tray_icon_light.gif")).getImage();

		createPopupMenu();

		createTray(this);

		this.setActionListener(controller);

		JPanel northPanel = this.createNorthPanel(this.ownPortText_, this.powerTButton_);
		JPanel centorPanel = this.createCentorPanel(this.displayArea_);
		JPanel southPanel = this.createSouthPanel(this.connectHostText_, this.connectPortText_, this.connectTButton_,
				this.messageText_, this.sendButton_);
		JPanel phonePanel = this.createPhonePanel(northPanel, centorPanel, southPanel);

		Container container = this.getContentPane();
		container.add(phonePanel);

	}

	/**
	 * タスクトレイに表示するデフォルトのPopupMenuメニューを生成します。 以下のメニューがあります。 ・「タスクトレイから取り出す」
	 * タスクトレイから対象のアプリケーションをデスクトップ上に取り出します。 （ウィンドウをアクティブにし、手前に表示する。）
	 * ・終了対象のアプリケーションを終了させます。
	 */
	private void createPopupMenu() {
		MenuItem getTrayItem = new MenuItem("表示する");
		getTrayItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				active();
			}
		});
		MenuItem exitItem = new MenuItem("終了");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closefromTray();
			}
		});
		defaultMenu = new PopupMenu();
		defaultMenu.add(getTrayItem);
		defaultMenu.add(exitItem);
	}

	/**
	 * タスクトレイから対象のアプリケーションをデスクトップ上に取り出します。
	 */
	private void active() {
		// フレームの状態を通常に戻す。
		this.frame.setExtendedState(JFrame.NORMAL);
		this.frame.setAlwaysOnTop(true);
		this.frame.setVisible(true);
		this.frame.setAlwaysOnTop(false);
	}

	private void closefromTray() {
		removeTrayIcon();
		this.frame.setVisible(false);
		this.frame.dispose();
		System.exit(0);
	}

	/**
	 * タスクトレイからアイコンを削除する。 アプリケーション終了時に呼び出す必要がある。
	 */
	public void removeTrayIcon() {
		tray.remove(trayIcon);
	}

	/**
	 * 表示ウィンドウに文字列を追加する。
	 * 
	 * @param text
	 *            追加する文字列
	 */
	public synchronized void appendText(String text) {
		this.displayArea_.append(text + "\n");
		this.distplayScrollPane_.getViewport().scrollRectToVisible(new Rectangle(0, Integer.MAX_VALUE - 1, 1, 1));
		this.displayArea_.setCaretPosition(this.displayArea_.getDocument().getLength());
	}

	/**
	 * 送信メッセージをクリアする。
	 */
	public synchronized void clearMsg() {
		this.messageText_.setText("");
	}

	/**
	 * エラーダイアログを表示する。
	 * 
	 * @param errMsg
	 *            エラーメッセージ
	 */
	public synchronized void showErrorDialog(String errMsg) {
		System.err.println(errMsg);
		try {
			this.appendText(new String(errMsg.getBytes(PhoneConstant.ENCODE_UTF_8), PhoneConstant.ENCODE_UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 携帯電話ウィンドウの描画をステータスに合わせて更新する。
	 * 
	 * @param status
	 *            変化後のステータス
	 */
	public void setViewByStatus(PhoneStatus status) {
		boolean isOwnPortTextEditable = false;
		boolean isOwnPortTButtonSelected = false;
		boolean isDisplayEnabled = false;
		boolean isConnectionTextEditable = false;
		boolean isConnectionTButtonEnabled = false;
		boolean isConnectionTButtonSelected = false;
		boolean isMessageTextEditable = false;
		boolean isMessageButtonEnabled = false;
		boolean isP2PCheckBoxEnabled = false;
		boolean isGroupCheckBoxEnabled = true;

		String ownPortLabel = "電源";
		String connectionLabel = "未接続";
		// System.out.println(PhoneController.numConnection_);
		switch (status) {
		case POWER_OFF: // 電源が入っていない状態
		{
			isOwnPortTextEditable = true;
			this.displayArea_.setText("");
			this.connectHostText_.setText("");
			this.connectPortText_.setText("");

			break;
		}
		case NO_CONNECTION: // 接続がない状態
		{
			if (PhoneController.numConnection_ <= 0) {
				isOwnPortTButtonSelected = true;
				isDisplayEnabled = true;
				isConnectionTextEditable = true;
				isConnectionTButtonEnabled = true;
				this.messageText_.setText("");
				if (this.phoneController_.isPhoneP2PMode_()) {
					isP2PCheckBoxEnabled = true;
					isGroupCheckBoxEnabled = false;
				} else if (this.phoneController_.isPhoneGroupMode_()) {
					isP2PCheckBoxEnabled = false;
					isGroupCheckBoxEnabled = true;
				} else {
					isP2PCheckBoxEnabled = true;
					isGroupCheckBoxEnabled = true;
				}

				ownPortLabel = "電源OFF";
				connectionLabel = "接続開始";
				break;
			} else if (PhoneController.numConnection_ > 0) {
				// case CONNECTION:に移行
			}

		}
		case CONNECTION: // 接続中の状態
		{
			isOwnPortTButtonSelected = true;
			isDisplayEnabled = true;
			isConnectionTButtonEnabled = true;
			isConnectionTButtonSelected = true;
			isMessageTextEditable = true;
			isMessageButtonEnabled = true;
			if (this.phoneController_.isPhoneP2PMode_()) {
				isP2PCheckBoxEnabled = true;
				isGroupCheckBoxEnabled = false;
			} else if (this.phoneController_.isPhoneGroupMode_()) {
				isP2PCheckBoxEnabled = false;
				isGroupCheckBoxEnabled = true;
			} else {
				isP2PCheckBoxEnabled = true;
				isGroupCheckBoxEnabled = true;
			}

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
		this.ownPortText_.setEditable(isOwnPortTextEditable);
		this.powerTButton_.setSelected(isOwnPortTButtonSelected);
		this.powerTButton_.setText(ownPortLabel);

		this.displayArea_.setEnabled(isDisplayEnabled);

		// 接続設定
		this.connectHostText_.setEditable(isConnectionTextEditable);
		this.connectPortText_.setEditable(isConnectionTextEditable);
		this.connectTButton_.setSelected(isConnectionTButtonSelected);
		this.connectTButton_.setEnabled(isConnectionTButtonEnabled);
		this.connectTButton_.setText(connectionLabel);
		this.setP2PBox_.setEnabled(isP2PCheckBoxEnabled);
		this.asGroupHost_.setEnabled(isGroupCheckBoxEnabled);

		// 送信設定
		this.messageText_.setEditable(isMessageTextEditable);
		this.sendButton_.setEnabled(isMessageButtonEnabled);
	}

	/**
	 * 自ポートとして指定された文字列を返す。
	 * 
	 * @return 自ポート入力欄の文字列
	 */
	public String getOwnPortStr() {
		String ownPortStr = this.ownPortText_.getText();
		return ownPortStr;
	}

	/**
	 * 接続先として指定されたホストの文字列を返す。
	 * 
	 * @return 接続先ホストの文字列
	 */
	public String getConnectHostStr() {
		String hostStr = this.connectHostText_.getText();
		return hostStr;
	}

	/**
	 * 接続先として指定されたポートの文字列を返す。
	 * 
	 * @return 接続先ポートの文字列
	 */
	public String getConnectPortStr() {
		String portStr = this.connectPortText_.getText();
		return portStr;
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

		this.ownPortText_ = new JTextField();
		this.ownPortText_.setColumns(10);
		this.connectHostText_ = new JTextField();
		this.connectHostText_.setColumns(15);
		this.connectPortText_ = new JTextField();
		this.connectPortText_.setColumns(6);
		this.messageText_ = new JTextField();
		this.messageText_.setColumns(29);
		this.displayArea_ = new JTextArea();
		this.displayArea_.setEditable(false);
		this.distplayScrollPane_ = new JScrollPane(this.displayArea_);
		getContentPane().add(distplayScrollPane_);

		this.powerTButton_ = new JToggleButton("電源ON");
		this.connectTButton_ = new JToggleButton("未接続");
		this.sendButton_ = new JButton("送信");
		this.setP2PBox_ = new JCheckBox("P2P Mode", true);
		this.asGroupHost_ = new JCheckBox("Group Host Mode", false);

		this.powerTButton_.setPreferredSize(BUTTON_PERFORMED_SIZE);
		this.connectTButton_.setPreferredSize(BUTTON_PERFORMED_SIZE);
		this.sendButton_.setPreferredSize(BUTTON_PERFORMED_SIZE);

		this.powerTButton_.setActionCommand(PhoneConstant.CMD_POWERED_BUTTON);
		this.connectTButton_.setActionCommand(PhoneConstant.CMD_CONNECT_SERVER_BUTTON);
		this.sendButton_.setActionCommand(PhoneConstant.CMD_SEND_MESSAGE_BUTTON);

		this.setViewByStatus(PhoneStatus.POWER_OFF);

		this.setTitle(FRAME_TITLE);
		this.setMinimumSize(FRAME_MINIMUM_SIZE);
		this.setIconImage(ICON);
	}

	/**
	 * アクションリスナを設定する。
	 * 
	 * @param controller
	 *            携帯電話アプリ管理
	 */
	private void setActionListener(PhoneController controller) {
		PhoneListener listener = new PhoneListener(controller);
		this.powerTButton_.addActionListener(listener);
		this.connectTButton_.addActionListener(listener);
		this.sendButton_.addActionListener(listener);
		this.setP2PBox_.addItemListener(listener);
		this.asGroupHost_.addItemListener(listener);
		this.messageText_.addKeyListener(listener);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
				frame.setVisible(false);
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
				replaceImageWithDelete(frame.getIconImage());
				// frame.removeWindowListener(frame.getWindowListeners());
				for(int i = 1; i< trayIcon.getMouseListeners().length; i++){
					 trayIcon.removeMouseListener(trayIcon.getMouseListeners()[i]);
				}

			}
		});
		this.displayArea_.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!frame.isActive()) {
					onAndOffLight(imgLight, null);
					displayMessage("WeChatメッセージ", "メッセージを確認してしてください！！");
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!frame.isActive()) {
					onAndOffLight(imgLight, null);
					displayMessage("WeChatメッセージ", "メッセージを確認してしてください！！");
				}
			}
		});
	}

	/**
	 * 携帯電話ウィンドウの自ポート番号を入力するパネルを作成する。
	 * 
	 * @param portText
	 *            自ポート番号入力フィールド
	 * @param powerButton
	 *            接続待ち設定ボタン
	 * @return 自ポート番号入力パネル
	 */
	private JPanel createNorthPanel(JTextField portText, AbstractButton powerButton) {
		JPanel returnPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		returnPanel.setLayout(flowLayout);
		JLabel portLabel = new JLabel("PORT");

		returnPanel.add(portLabel);
		returnPanel.add(portText);
		returnPanel.add(powerButton);

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
		JPanel functionBoxPanel = new JPanel();
		functionBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		functionBoxPanel.add(this.setP2PBox_);
		functionBoxPanel.add(this.asGroupHost_);
		returnPanel.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(displayArea);
		returnPanel.add(scrollPane, BorderLayout.CENTER);
		returnPanel.add(functionBoxPanel, BorderLayout.NORTH);

		return returnPanel;
	}

	/**
	 * 携帯電話ウィンドウの送信設定パネルを作成する。
	 * 
	 * @param hostText
	 *            送信先ホストを入力するテキストフィールド
	 * @param portText
	 *            送信先ポートを入力するテキストフィールド
	 * @param connectButton
	 *            接続ボタン
	 * @param messageText
	 *            送信メッセージを入力するテキストフィールド
	 * @param sendButton
	 *            送信ボタン
	 * @return 送信設定パネル
	 */
	private JPanel createSouthPanel(JTextField hostText, JTextField portText, AbstractButton connectButton,
			JTextField messageText, AbstractButton sendButton) {
		// 送信先設定パネルの生成
		JPanel connectPanel = new JPanel();
		connectPanel.setLayout(new FlowLayout());

		JLabel hostLabel = new JLabel("接続先HOST");
		JLabel portLabel = new JLabel("接続先PORT");

		connectPanel.add(hostLabel);
		connectPanel.add(hostText);
		connectPanel.add(portLabel);
		connectPanel.add(portText);
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

	/**
	 * タスクトレイのアイコンを点滅させる。
	 * 
	 * @param msg
	 *            必要であれば点滅されたアイコンがクリックされた時に 表示するメッセージをセットします。
	 */
	public void onAndOffLight(Image lightImg, String msg) {
		replaceImage(lightImg);
		trayIcon.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				displayMessage(null, "Wechatを呼び出しました！");
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	/**
	 * タスクトレイにポップアップ・メッセージを表示させます。
	 * 
	 * @param caption
	 * @param text
	 */
	public void displayMessage(String caption, String text) {
		if (caption != null || text != null) {
			this.trayIcon.displayMessage(caption, text, TrayIcon.MessageType.INFO);
		}
	}

	/**
	 * タスクトレイのアイコンを変更する。
	 * 
	 * @param image
	 *            現在のアイコンと違う場合のみ変更する。
	 */
	public void replaceImage(Image image) {
		synchronized (INSTANCE_LOCK) {
			if (!image.equals(this.trayIcon.getImage())) {
				this.trayIcon.setImage(image);
			}
		}
	}

	/**
	 * トレイアイコンを作成し、システムトレイに追加する。 アイコン上で発生したイベントに対するリスナを targetFrameに設定します。
	 * ここでのイベントは、トレイ・アイコンをダブルクリックされた時に対象のアプリケーションを取り出します。
	 * 
	 * @param targetFrame
	 *            対象のアプリケーション
	 */
	public void createTray(JFrame targetFrame) {
		createTray(targetFrame, null, null);
	}

	/**
	 * トレイアイコンを作成し、システムトレイに追加する。
	 * 
	 * @param targetFrame
	 *            対象のアプリケーション
	 * @param image
	 *            トレイアイコンに表示するイメージ画像。 nullの場合、targetFrame からgetIconImage()で取得できる
	 *            イメージ画像を使用します。
	 * @param menu
	 *            タスクトレイに表示するPopupMenu。 nullの場合、デフォルトのPopupMenuを表示します。
	 */
	public void createTray(final JFrame targetFrame, Image image, PopupMenu menu) {
		// システム・トレイがサポートされていなければ終了。
		if (!SystemTray.isSupported()) {
			return;
		}
		this.frame = targetFrame;
		if (image == null) {
			image = targetFrame.getIconImage();
		}
		if (menu == null) {
			menu = defaultMenu;
		}
		trayIcon = new TrayIcon(image, targetFrame.getTitle(), menu);
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					active();
				}
			}
		});
		try {
			tray.add(trayIcon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * タスクトレイのアイコンを変更する。 ※何故か点滅しているアイコンを戻す場合は、
	 * {@link SystemTray#remove(TrayIcon)})で削除しないと 駄目なようである。
	 * 
	 * @param image
	 *            現在のアイコンと違う場合のみ変更する。
	 */
	public void replaceImageWithDelete(Image image) {
		synchronized (INSTANCE_LOCK) {
			if (!image.equals(trayIcon.getImage())) {
				tray.remove(trayIcon);
				trayIcon.setImage(image);
				try {
					tray.add(trayIcon);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
