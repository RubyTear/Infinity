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
	 * ���s�T���v��
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// LookAndFeel<b
			// style="color:black;background-color:#99ff99">��</b>Windows<b
			// style="color:white;background-color:#880000">��</b>�ݒ�
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// �ʏ��<b style="color:black;background-color:#a0ffff">�A�C�R��</b>
			final Image img = new ImageIcon(
					TaskTrayUtil.class.getResource("../tray_icon.gif"))
					.getImage();
			final JFrame frame = new JFrame("�^�X�N�g���C�ɃA�C�R����\���E�_��");
			frame.setSize(300, 100);
			frame.setIconImage(img);
			// �E�B���h�E���ŏ������ꂽ���Ƀ^�X�N�g���C<b
			// style="color:white;background-color:#880000">��</b>�i�[����
			// <b style="color:black;background-color:#ff66ff">�悤</b><b
			// style="color:white;background-color:#880000">��</b>WindowListener�N���X<b
			// style="color:black;background-color:#99ff99">��</b>�ǉ��B
			frame.addWindowListener(new WindowAdapter() {
				public void windowIconified(WindowEvent e) {
					frame.setVisible(false);
				}
			});

			JPanel jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout());
			jPanel.setSize(new Dimension(219, 70));
			// �_��<b style="color:black;background-color:#a0ffff">�A�C�R��</b>
			final Image imgLight = new ImageIcon(
					TaskTrayUtil.class.getResource("../tray_icon_light.gif"))
					.getImage();
			JButton lightButton = new JButton("�_��");
			lightButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onAndOffLight(imgLight, null);
					displayMessage("���b�Z�[�W", "��������o���Ă���I�I");
				}
			});
			JButton cancelButton = new JButton("����");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					replaceImageWithDelete(img);
				}
			});
			jPanel.add(lightButton, null);
			jPanel.add(cancelButton, null);
			frame.add(jPanel);
			// �I���������B
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// �t���[��<b style="color:black;background-color:#99ff99">��</b>��ʒ���<b
			// style="color:white;background-color:#880000">��</b>�\���B
			frame.setLocationRelativeTo(null);
			// �^�X�N�g���C<b style="color:black;background-color:#99ff99">��</b>�쐬����B
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
	 * �^�X�N�g���C<b style="color:white;background-color:#880000">��</b>
	 * �\������f�t�H���g��PopupMenu���j���[<b
	 * style="color:black;background-color:#99ff99">��</b>�������܂��B �ȉ��̃��j���[������܂��B
	 * �E�u�^�X�N�g���C������o���v �^�X�N�g���C����Ώۂ̃A�v���P�[�V����<b
	 * style="color:black;background-color:#99ff99">��</b>�f�X�N�g�b�v��<b
	 * style="color:white;background-color:#880000">��</b> ���o���܂��B �i�E�B���h�E<b
	 * style="color:black;background-color:#99ff99">��</b>�A�N�e�B�u�ɂ��A��O<b
	 * style="color:white;background-color:#880000">��</b>�\������B�j �E�I���Ώۂ̃A�v���P�[�V����<b
	 * style="color:black;background-color:#99ff99">��</b>�I�������܂��B
	 */
	private static void createPopupMenu() {
		MenuItem getTrayItem = new MenuItem("�^�X�N�g���C������o��");
		getTrayItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				active();
			}
		});
		MenuItem exitItem = new MenuItem("�I��");
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
	 * �g���C<b style="color:black;background-color:#a0ffff">�A�C�R��</b><b
	 * style="color:black;background-color:#99ff99">��</b>�쐬���A�V�X�e���g���C<b
	 * style="color:white;background-color:#880000">��</b>�ǉ�����B <b
	 * style="color:black;background-color:#a0ffff">�A�C�R��</b>��Ŕ��������C�x���g�ɑ΂��郊�X�i<b
	 * style="color:black;background-color:#99ff99">��</b> targetFrame <b
	 * style="color:white;background-color:#880000">��</b>�ݒ肵�܂��B �����ł̃C�x���g�́A�g���C�E<b
	 * style="color:black;background-color:#a0ffff">�A�C�R��</b><b
	 * style="color:black;background-color:#99ff99">��</b>�_�u���N���b�N���ꂽ����
	 * �Ώۂ̃A�v���P�[�V����<b style="color:black;background-color:#99ff99">��</b>���o���܂��B
	 * 
	 * @param targetFrame
	 *            �Ώۂ̃A�v���P�[�V����
	 */
	public static void createTray(JFrame targetFrame) {
		createTray(targetFrame, null, null);
	}

	/**
	 * �g���C<b style="color:black;background-color:#a0ffff">�A�C�R��</b><b
	 * style="color:black;background-color:#99ff99">��</b>�쐬���A�V�X�e���g���C<b
	 * style="color:white;background-color:#880000">��</b>�ǉ�����B
	 * 
	 * @param targetFrame
	 *            �Ώۂ̃A�v���P�[�V����
	 * @param image
	 *            �g���C<b style="color:black;background-color:#a0ffff">�A�C�R��</b><b
	 *            style="color:white;background-color:#880000">��</b>�\������C���[�W�摜�B
	 *            null�̏ꍇ�AtargetFrame ����getIconImage()�Ŏ擾�ł��� �C���[�W�摜<b
	 *            style="color:black;background-color:#99ff99">��</b>�g�p���܂��B
	 * @param menu
	 *            �^�X�N�g���C<b style="color:white;background-color:#880000">��</b>
	 *            �\������PopupMenu�B null�̏ꍇ�A�f�t�H���g��PopupMenu<b
	 *            style="color:black;background-color:#99ff99">��</b>�\�����܂��B
	 */
	public static void createTray(final JFrame targetFrame, Image image,
			PopupMenu menu) {
		// �V�X�e���E�g���C���T�|�[�g����Ă��Ȃ���ΏI���B
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
	 * �^�X�N�g���C����<b style="color:black;background-color:#a0ffff">�A�C�R��</b><b
	 * style="color:black;background-color:#99ff99">��</b>�폜����B
	 * �A�v���P�[�V�����I�����ɌĂяo���K�v������B
	 */
	public static void removeTrayIcon() {
		tray.remove(trayIcon);
	}

	/**
	 * �^�X�N�g���C��<b style="color:black;background-color:#a0ffff">�A�C�R��</b><b
	 * style="color:black;background-color:#99ff99">��</b>�_�ł�����B
	 * 
	 * @param msg
	 *            �K�v�ł���Γ_�ł��ꂽ<b
	 *            style="color:black;background-color:#a0ffff">�A�C�R��
	 *            </b>���N���b�N���ꂽ���� �\�����郁�b�Z�[�W<b
	 *            style="color:black;background-color:#99ff99">��</b>�Z�b�g���܂��B
	 */
	public static void onAndOffLight(Image lightImg, String msg) {
		replaceImage(lightImg);
		ChangeLightImgAdapter adap = new TaskTrayUtil().new ChangeLightImgAdapter(
				msg);
		trayIcon.addMouseListener(adap);
		frame.addWindowListener(adap);
	}

	/**
	 * �^�X�N�g���C��<b style="color:black;background-color:#a0ffff">�A�C�R��</b><b
	 * style="color:black;background-color:#99ff99">��</b>�ύX����B
	 * 
	 * @param image
	 *            ���݂�<b style="color:black;background-color:#a0ffff">�A�C�R��</b>
	 *            �ƈႤ�ꍇ�̂ݕύX����B
	 */
	public static void replaceImage(Image image) {
		synchronized (INSTANCE_LOCK) {
			if (!image.equals(trayIcon.getImage())) {
				trayIcon.setImage(image);
			}
		}
	}

	/**
	 * �^�X�N�g���C��<b style="color:black;background-color:#a0ffff">�A�C�R��</b><b
	 * style="color:black;background-color:#99ff99">��</b>�ύX����B �����̂��_�ł��Ă���<b
	 * style="color:black;background-color:#a0ffff">�A�C�R��</b><b
	 * style="color:black;background-color:#99ff99">��</b>�߂��ꍇ�́A
	 * {@link SystemTray#remove(TrayIcon)})�ō폜���Ȃ��� �ʖڂ�<b
	 * style="color:black;background-color:#ff66ff">�悤</b>�ł���B
	 * 
	 * @param image
	 *            ���݂�<b style="color:black;background-color:#a0ffff">�A�C�R��</b>
	 *            �ƈႤ�ꍇ�̂ݕύX����B
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
	 * �^�X�N�g���C<b style="color:white;background-color:#880000">��</b>�|�b�v�A�b�v�E���b�Z�[�W<b
	 * style="color:black;background-color:#99ff99">��</b>�\�������܂��B
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
	 * �^�X�N�g���C����Ώۂ̃A�v���P�[�V����<b
	 * style="color:black;background-color:#99ff99">��</b>�f�X�N�g�b�v��<b
	 * style="color:white;background-color:#880000">��</b> ���o���܂��B
	 */
	private static void active() {
		// �t���[���̏��<b style="color:black;background-color:#99ff99">��</b>�ʏ�<b
		// style="color:white;background-color:#880000">��</b>�߂��B
		TaskTrayUtil.frame.setExtendedState(JFrame.NORMAL);
		TaskTrayUtil.frame.setAlwaysOnTop(true);
		TaskTrayUtil.frame.setVisible(true);
		TaskTrayUtil.frame.setAlwaysOnTop(false);
	}

	/**
	 * �_�ł��ꂽ<b style="color:black;background-color:#a0ffff">�A�C�R��</b>���N���b�N���ꂽ���ɁA
	 * ����������C�x���g Listener�N���X�B
	 */
	class ChangeLightImgAdapter implements WindowListener, MouseListener {
		String lightImageText;

		public ChangeLightImgAdapter(String lightImageText) {
			this.lightImageText = lightImageText;
		}

		/**
		 * �^�X�N�g���C<b
		 * style="color:black;background-color:#a0ffff">�A�C�R��</b>���N���b�N���ꂽ���� <b
		 * style="color:white;background-color:#880000">��</b> �|�b�v�A�b�v�E���b�Z�[�W<b
		 * style="color:black;background-color:#99ff99">��</b>�\������B
		 */
		public void mousePressed(MouseEvent e) {
			TaskTrayUtil.displayMessage(null, lightImageText);
		}

		/**
		 * �Ώۂ̃A�v���P�[�V�������A�N�e�B�u<b
		 * style="color:white;background-color:#880000">��</b>�Ȃ������ɓ_�ł��� <b
		 * style="color:black;background-color:#a0ffff">�A�C�R��</b><b
		 * style="color:black;background-color:#99ff99">��</b>�߂��B
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