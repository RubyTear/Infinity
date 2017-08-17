package sample;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TaskTrayUtil {
	private static JFrame frame;
	private static PopupMenu defaultMenu;
	private static TrayIcon trayIcon;
	private static SystemTray tray = SystemTray.getSystemTray();
	private static Object INSTANCE_LOCK = new Object();

	/**
	 * 実行サンプル
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// LookAndFeel<b
			// style="color:black;background-color:#99ff99">を</b>Windows<b
			// style="color:white;background-color:#880000">に</b>設定
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// 通常の<b style="color:black;background-color:#a0ffff">アイコン</b>
			final Image img = new ImageIcon(
					TaskTrayUtil.class.getResource("../tray_icon.gif"))
					.getImage();
			final JFrame frame = new JFrame("タスクトレイにアイコンを表示・点滅");
			frame.setSize(300, 100);
			frame.setIconImage(img);
			// ウィンドウが最小化された時にタスクトレイ<b
			// style="color:white;background-color:#880000">に</b>格納する
			// <b style="color:black;background-color:#ff66ff">よう</b><b
			// style="color:white;background-color:#880000">に</b>WindowListenerクラス<b
			// style="color:black;background-color:#99ff99">を</b>追加。
			frame.addWindowListener(new WindowAdapter() {
				public void windowIconified(WindowEvent e) {
					frame.setVisible(false);
				}
			});

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout());
			jPanel.setSize(new Dimension(219, 70));
			// 点滅<b style="color:black;background-color:#a0ffff">アイコン</b>
			final Image imgLight = new ImageIcon(
					TaskTrayUtil.class.getResource("../tray_icon_light.gif"))
					.getImage();
			JButton lightButton = new JButton("点滅");
			lightButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onAndOffLight(imgLight, null);
					displayMessage("メッセージ", "ここから出してくれ！！");
				}
			});
			JButton cancelButton = new JButton("解除");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					replaceImageWithDelete(img);
				}
			});
			jPanel.add(lightButton, null);
			jPanel.add(cancelButton, null);
			frame.add(jPanel);
			// 終了時処理。
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// フレーム<b style="color:black;background-color:#99ff99">を</b>画面中央<b
			// style="color:white;background-color:#880000">に</b>表示。
			frame.setLocationRelativeTo(null);
			// タスクトレイ<b style="color:black;background-color:#99ff99">を</b>作成する。
			TaskTrayUtil.createTray(frame);
			frame.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * 
     */
	static {
		createPopupMenu();
	}

	/**
	 * タスクトレイ<b style="color:white;background-color:#880000">に</b>
	 * 表示するデフォルトのPopupMenuメニュー<b
	 * style="color:black;background-color:#99ff99">を</b>生成します。 以下のメニューがあります。
	 * ・「タスクトレイから取り出す」 タスクトレイから対象のアプリケーション<b
	 * style="color:black;background-color:#99ff99">を</b>デスクトップ上<b
	 * style="color:white;background-color:#880000">に</b> 取り出します。 （ウィンドウ<b
	 * style="color:black;background-color:#99ff99">を</b>アクティブにし、手前<b
	 * style="color:white;background-color:#880000">に</b>表示する。） ・終了対象のアプリケーション<b
	 * style="color:black;background-color:#99ff99">を</b>終了させます。
	 */
	private static void createPopupMenu() {
		MenuItem getTrayItem = new MenuItem("タスクトレイから取り出す");
		getTrayItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				active();
			}
		});
		MenuItem exitItem = new MenuItem("終了");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeTrayIcon();
				TaskTrayUtil.frame.setVisible(false);
				TaskTrayUtil.frame.dispose();
				System.exit(0);
			}
		});
		defaultMenu = new PopupMenu();
		defaultMenu.add(getTrayItem);
		defaultMenu.add(exitItem);
	}

	/**
	 * トレイ<b style="color:black;background-color:#a0ffff">アイコン</b><b
	 * style="color:black;background-color:#99ff99">を</b>作成し、システムトレイ<b
	 * style="color:white;background-color:#880000">に</b>追加する。 <b
	 * style="color:black;background-color:#a0ffff">アイコン</b>上で発生したイベントに対するリスナ<b
	 * style="color:black;background-color:#99ff99">を</b> targetFrame <b
	 * style="color:white;background-color:#880000">に</b>設定します。 ここでのイベントは、トレイ・<b
	 * style="color:black;background-color:#a0ffff">アイコン</b><b
	 * style="color:black;background-color:#99ff99">を</b>ダブルクリックされた時に
	 * 対象のアプリケーション<b style="color:black;background-color:#99ff99">を</b>取り出します。
	 * 
	 * @param targetFrame
	 *            対象のアプリケーション
	 */
	public static void createTray(JFrame targetFrame) {
		createTray(targetFrame, null, null);
	}

	/**
	 * トレイ<b style="color:black;background-color:#a0ffff">アイコン</b><b
	 * style="color:black;background-color:#99ff99">を</b>作成し、システムトレイ<b
	 * style="color:white;background-color:#880000">に</b>追加する。
	 * 
	 * @param targetFrame
	 *            対象のアプリケーション
	 * @param image
	 *            トレイ<b style="color:black;background-color:#a0ffff">アイコン</b><b
	 *            style="color:white;background-color:#880000">に</b>表示するイメージ画像。
	 *            nullの場合、targetFrame からgetIconImage()で取得できる イメージ画像<b
	 *            style="color:black;background-color:#99ff99">を</b>使用します。
	 * @param menu
	 *            タスクトレイ<b style="color:white;background-color:#880000">に</b>
	 *            表示するPopupMenu。 nullの場合、デフォルトのPopupMenu<b
	 *            style="color:black;background-color:#99ff99">を</b>表示します。
	 */
	public static void createTray(final JFrame targetFrame, Image image,
			PopupMenu menu) {
		// システム・トレイがサポートされていなければ終了。
		if (!SystemTray.isSupported()) {
			return;
		}
		TaskTrayUtil.frame = targetFrame;
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
	 * タスクトレイから<b style="color:black;background-color:#a0ffff">アイコン</b><b
	 * style="color:black;background-color:#99ff99">を</b>削除する。
	 * アプリケーション終了時に呼び出す必要がある。
	 */
	public static void removeTrayIcon() {
		tray.remove(trayIcon);
	}

	/**
	 * タスクトレイの<b style="color:black;background-color:#a0ffff">アイコン</b><b
	 * style="color:black;background-color:#99ff99">を</b>点滅させる。
	 * 
	 * @param msg
	 *            必要であれば点滅された<b
	 *            style="color:black;background-color:#a0ffff">アイコン
	 *            </b>がクリックされた時に 表示するメッセージ<b
	 *            style="color:black;background-color:#99ff99">を</b>セットします。
	 */
	public static void onAndOffLight(Image lightImg, String msg) {
		replaceImage(lightImg);
		ChangeLightImgAdapter adap = new TaskTrayUtil().new ChangeLightImgAdapter(
				msg);
		trayIcon.addMouseListener(adap);
		frame.addWindowListener(adap);
	}

	/**
	 * タスクトレイの<b style="color:black;background-color:#a0ffff">アイコン</b><b
	 * style="color:black;background-color:#99ff99">を</b>変更する。
	 * 
	 * @param image
	 *            現在の<b style="color:black;background-color:#a0ffff">アイコン</b>
	 *            と違う場合のみ変更する。
	 */
	public static void replaceImage(Image image) {
		synchronized (INSTANCE_LOCK) {
			if (!image.equals(trayIcon.getImage())) {
				trayIcon.setImage(image);
			}
		}
	}

	/**
	 * タスクトレイの<b style="color:black;background-color:#a0ffff">アイコン</b><b
	 * style="color:black;background-color:#99ff99">を</b>変更する。 ※何故か点滅している<b
	 * style="color:black;background-color:#a0ffff">アイコン</b><b
	 * style="color:black;background-color:#99ff99">を</b>戻す場合は、
	 * {@link SystemTray#remove(TrayIcon)})で削除しないと 駄目な<b
	 * style="color:black;background-color:#ff66ff">よう</b>である。
	 * 
	 * @param image
	 *            現在の<b style="color:black;background-color:#a0ffff">アイコン</b>
	 *            と違う場合のみ変更する。
	 */
	public static void replaceImageWithDelete(Image image) {
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

	/**
	 * タスクトレイ<b style="color:white;background-color:#880000">に</b>ポップアップ・メッセージ<b
	 * style="color:black;background-color:#99ff99">を</b>表示させます。
	 * 
	 * @param caption
	 * @param text
	 */
	public static void displayMessage(String caption, String text) {
		if (caption != null || text != null) {
			trayIcon.displayMessage(caption, text, TrayIcon.MessageType.INFO);
		}
	}

	/**
	 * タスクトレイから対象のアプリケーション<b
	 * style="color:black;background-color:#99ff99">を</b>デスクトップ上<b
	 * style="color:white;background-color:#880000">に</b> 取り出します。
	 */
	private static void active() {
		// フレームの状態<b style="color:black;background-color:#99ff99">を</b>通常<b
		// style="color:white;background-color:#880000">に</b>戻す。
		TaskTrayUtil.frame.setExtendedState(JFrame.NORMAL);
		TaskTrayUtil.frame.setAlwaysOnTop(true);
		TaskTrayUtil.frame.setVisible(true);
		TaskTrayUtil.frame.setAlwaysOnTop(false);
	}

	/**
	 * 点滅された<b style="color:black;background-color:#a0ffff">アイコン</b>がクリックされた時に、
	 * 発生させるイベント Listenerクラス。
	 */
	class ChangeLightImgAdapter implements WindowListener, MouseListener {
		String lightImageText;

		public ChangeLightImgAdapter(String lightImageText) {
			this.lightImageText = lightImageText;
		}

		/**
		 * タスクトレイ<b
		 * style="color:black;background-color:#a0ffff">アイコン</b>がクリックされた時に <b
		 * style="color:white;background-color:#880000">に</b> ポップアップ・メッセージ<b
		 * style="color:black;background-color:#99ff99">を</b>表示する。
		 */
		public void mousePressed(MouseEvent e) {
			TaskTrayUtil.displayMessage(null, lightImageText);
		}

		/**
		 * 対象のアプリケーションがアクティブ<b
		 * style="color:white;background-color:#880000">に</b>なった時に点滅した <b
		 * style="color:black;background-color:#a0ffff">アイコン</b><b
		 * style="color:black;background-color:#99ff99">を</b>戻す。
		 */
		public void windowActivated(WindowEvent e) {
			TaskTrayUtil.replaceImageWithDelete(TaskTrayUtil.frame
					.getIconImage());
			TaskTrayUtil.frame.removeWindowListener(this);
			TaskTrayUtil.trayIcon.removeMouseListener(this);
		}

		public void windowClosed(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
		}

		public void windowDeactivated(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowOpened(WindowEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}
}