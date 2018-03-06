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
 * �g�ѓd�b�̕\���E�B���h�E��\���B
 * 
 */
public class PhoneFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** �E�B���h�E�̃^�C�g��������B */
	private static final String FRAME_TITLE = "WeChat (Socket)";

	/** �E�B���h�E�̍ŏ��T�C�Y�B */
	private static final Dimension FRAME_MINIMUM_SIZE = new Dimension(500, 350);

	/** �{�^���̃f�t�H���g�T�C�Y�B */
	private static final Dimension BUTTON_PERFORMED_SIZE = new Dimension(86, 27);

	/** �t�B�[���h������������Ă��邩���f����t���O�B */
	private boolean isFieldInitialized_;

	/** ���|�[�g�ԍ���ݒ肷��e�L�X�g�t�B�[���h�B */
	private JTextField ownPortText_;

	/** �ڑ���̃z�X�g��ݒ肷��e�L�X�g�t�B�[���h�B */
	private JTextField connectHostText_;

	/** �ڑ���̃|�[�g�ԍ���ݒ肷��e�L�X�g�t�B�[���h�B */
	private JTextField connectPortText_;

	/** ���M���b�Z�[�W����͂���e�L�X�g�t�B�[���h�B */
	private JTextField messageText_;

	/** ����M���b�Z�[�W��\������e�L�X�g�G���A�B */
	private JTextArea displayArea_;

	/** ����M���b�Z�[�W��ScrollPane�B */
	private JScrollPane distplayScrollPane_;

	/** �\�P�b�g��M�҂���Ԃ̐ݒ���s���g�O���{�^���B */
	private JToggleButton powerTButton_;

	/** �ڑ���Ԃ̐ݒ���s���g�O���{�^���B */
	private JToggleButton connectTButton_;

	/** ���b�Z�[�W�𑗐M����{�^���B */
	private JButton sendButton_;

	/** ���b�Z�[�W�𑗐M����{�^���B */
	private JCheckBox setP2PBox_;

	/** ���b�Z�[�W�𑗐M����{�^���B */
	private JCheckBox asGroupHost_;

	/** iconImage�擾�B */
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
	 * �R���X�g���N�^�B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ�
	 */
	public PhoneFrame(PhoneController controller) {

		this.phoneController_ = controller;

		// �t�B�[���h�̏�����
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
	 * �^�X�N�g���C�ɕ\������f�t�H���g��PopupMenu���j���[�𐶐����܂��B �ȉ��̃��j���[������܂��B �E�u�^�X�N�g���C������o���v
	 * �^�X�N�g���C����Ώۂ̃A�v���P�[�V�������f�X�N�g�b�v��Ɏ��o���܂��B �i�E�B���h�E���A�N�e�B�u�ɂ��A��O�ɕ\������B�j
	 * �E�I���Ώۂ̃A�v���P�[�V�������I�������܂��B
	 */
	private void createPopupMenu() {
		MenuItem getTrayItem = new MenuItem("�\������");
		getTrayItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				active();
			}
		});
		MenuItem exitItem = new MenuItem("�I��");
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
	 * �^�X�N�g���C����Ώۂ̃A�v���P�[�V�������f�X�N�g�b�v��Ɏ��o���܂��B
	 */
	private void active() {
		// �t���[���̏�Ԃ�ʏ�ɖ߂��B
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
	 * �^�X�N�g���C����A�C�R�����폜����B �A�v���P�[�V�����I�����ɌĂяo���K�v������B
	 */
	public void removeTrayIcon() {
		tray.remove(trayIcon);
	}

	/**
	 * �\���E�B���h�E�ɕ������ǉ�����B
	 * 
	 * @param text
	 *            �ǉ����镶����
	 */
	public synchronized void appendText(String text) {
		this.displayArea_.append(text + "\n");
		this.distplayScrollPane_.getViewport().scrollRectToVisible(new Rectangle(0, Integer.MAX_VALUE - 1, 1, 1));
		this.displayArea_.setCaretPosition(this.displayArea_.getDocument().getLength());
	}

	/**
	 * ���M���b�Z�[�W���N���A����B
	 */
	public synchronized void clearMsg() {
		this.messageText_.setText("");
	}

	/**
	 * �G���[�_�C�A���O��\������B
	 * 
	 * @param errMsg
	 *            �G���[���b�Z�[�W
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
	 * �g�ѓd�b�E�B���h�E�̕`����X�e�[�^�X�ɍ��킹�čX�V����B
	 * 
	 * @param status
	 *            �ω���̃X�e�[�^�X
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

		String ownPortLabel = "�d��";
		String connectionLabel = "���ڑ�";
		// System.out.println(PhoneController.numConnection_);
		switch (status) {
		case POWER_OFF: // �d���������Ă��Ȃ����
		{
			isOwnPortTextEditable = true;
			this.displayArea_.setText("");
			this.connectHostText_.setText("");
			this.connectPortText_.setText("");

			break;
		}
		case NO_CONNECTION: // �ڑ����Ȃ����
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

				ownPortLabel = "�d��OFF";
				connectionLabel = "�ڑ��J�n";
				break;
			} else if (PhoneController.numConnection_ > 0) {
				// case CONNECTION:�Ɉڍs
			}

		}
		case CONNECTION: // �ڑ����̏��
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

			ownPortLabel = "�d��OFF";
			connectionLabel = "�ڑ����f";

			break;
		}

		default: {
			System.err.println("�ُ�ȃX�e�[�^�X");
			break;
		}
		}

		// ���|�[�g�ݒ�
		this.ownPortText_.setEditable(isOwnPortTextEditable);
		this.powerTButton_.setSelected(isOwnPortTButtonSelected);
		this.powerTButton_.setText(ownPortLabel);

		this.displayArea_.setEnabled(isDisplayEnabled);

		// �ڑ��ݒ�
		this.connectHostText_.setEditable(isConnectionTextEditable);
		this.connectPortText_.setEditable(isConnectionTextEditable);
		this.connectTButton_.setSelected(isConnectionTButtonSelected);
		this.connectTButton_.setEnabled(isConnectionTButtonEnabled);
		this.connectTButton_.setText(connectionLabel);
		this.setP2PBox_.setEnabled(isP2PCheckBoxEnabled);
		this.asGroupHost_.setEnabled(isGroupCheckBoxEnabled);

		// ���M�ݒ�
		this.messageText_.setEditable(isMessageTextEditable);
		this.sendButton_.setEnabled(isMessageButtonEnabled);
	}

	/**
	 * ���|�[�g�Ƃ��Ďw�肳�ꂽ�������Ԃ��B
	 * 
	 * @return ���|�[�g���͗��̕�����
	 */
	public String getOwnPortStr() {
		String ownPortStr = this.ownPortText_.getText();
		return ownPortStr;
	}

	/**
	 * �ڑ���Ƃ��Ďw�肳�ꂽ�z�X�g�̕������Ԃ��B
	 * 
	 * @return �ڑ���z�X�g�̕�����
	 */
	public String getConnectHostStr() {
		String hostStr = this.connectHostText_.getText();
		return hostStr;
	}

	/**
	 * �ڑ���Ƃ��Ďw�肳�ꂽ�|�[�g�̕������Ԃ��B
	 * 
	 * @return �ڑ���|�[�g�̕�����
	 */
	public String getConnectPortStr() {
		String portStr = this.connectPortText_.getText();
		return portStr;
	}

	/**
	 * ���b�Z�[�W�e�L�X�g�ɓ��͂��ꂽ�������Ԃ��B
	 * 
	 * @return ���M���b�Z�[�W
	 */
	public String getSendMessage() {
		String message = this.messageText_.getText();
		return message;
	}

	/**
	 * �t�B�[���h�̏������������s���B ���̃��\�b�h�͌�����x�����Ăяo����Ȃ��B
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

		this.powerTButton_ = new JToggleButton("�d��ON");
		this.connectTButton_ = new JToggleButton("���ڑ�");
		this.sendButton_ = new JButton("���M");
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
	 * �A�N�V�������X�i��ݒ肷��B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ�
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
					displayMessage("WeChat���b�Z�[�W", "���b�Z�[�W���m�F���Ă��Ă��������I�I");
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!frame.isActive()) {
					onAndOffLight(imgLight, null);
					displayMessage("WeChat���b�Z�[�W", "���b�Z�[�W���m�F���Ă��Ă��������I�I");
				}
			}
		});
	}

	/**
	 * �g�ѓd�b�E�B���h�E�̎��|�[�g�ԍ�����͂���p�l�����쐬����B
	 * 
	 * @param portText
	 *            ���|�[�g�ԍ����̓t�B�[���h
	 * @param powerButton
	 *            �ڑ��҂��ݒ�{�^��
	 * @return ���|�[�g�ԍ����̓p�l��
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
	 * �g�ѓd�b�E�B���h�E�̕\����ʕt���p�l�����쐬����B
	 * 
	 * @param displayArea
	 *            �\�����
	 * @return �\����ʕt���p�l��
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
	 * �g�ѓd�b�E�B���h�E�̑��M�ݒ�p�l�����쐬����B
	 * 
	 * @param hostText
	 *            ���M��z�X�g����͂���e�L�X�g�t�B�[���h
	 * @param portText
	 *            ���M��|�[�g����͂���e�L�X�g�t�B�[���h
	 * @param connectButton
	 *            �ڑ��{�^��
	 * @param messageText
	 *            ���M���b�Z�[�W����͂���e�L�X�g�t�B�[���h
	 * @param sendButton
	 *            ���M�{�^��
	 * @return ���M�ݒ�p�l��
	 */
	private JPanel createSouthPanel(JTextField hostText, JTextField portText, AbstractButton connectButton,
			JTextField messageText, AbstractButton sendButton) {
		// ���M��ݒ�p�l���̐���
		JPanel connectPanel = new JPanel();
		connectPanel.setLayout(new FlowLayout());

		JLabel hostLabel = new JLabel("�ڑ���HOST");
		JLabel portLabel = new JLabel("�ڑ���PORT");

		connectPanel.add(hostLabel);
		connectPanel.add(hostText);
		connectPanel.add(portLabel);
		connectPanel.add(portText);
		connectPanel.add(connectButton);

		// ���M�p�l���̐���
		JPanel sendPanel = new JPanel();
		sendPanel.setLayout(new FlowLayout());

		JLabel sendLabel = new JLabel("���b�Z�[�W");

		sendPanel.add(sendLabel);
		sendPanel.add(messageText);
		sendPanel.add(sendButton);

		// ���M�ݒ�p�l���̐���
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new GridLayout(2, 1));
		returnPanel.add(connectPanel);
		returnPanel.add(sendPanel);

		return returnPanel;
	}

	/**
	 * �g�ѓd�b�E�B���h�E�̃��C���p�l���𐶐�����B
	 * 
	 * @param north
	 *            �㑤�ɕ\������p�l��
	 * @param centor
	 *            �����ɕ\������p�l��
	 * @param south
	 *            �����ɕ\������p�l��
	 * @return �g�ѓd�b�E�B���h�E
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
	 * �^�X�N�g���C�̃A�C�R����_�ł�����B
	 * 
	 * @param msg
	 *            �K�v�ł���Γ_�ł��ꂽ�A�C�R�����N���b�N���ꂽ���� �\�����郁�b�Z�[�W���Z�b�g���܂��B
	 */
	public void onAndOffLight(Image lightImg, String msg) {
		replaceImage(lightImg);
		trayIcon.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				displayMessage(null, "Wechat���Ăяo���܂����I");
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
	 * �^�X�N�g���C�Ƀ|�b�v�A�b�v�E���b�Z�[�W��\�������܂��B
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
	 * �^�X�N�g���C�̃A�C�R����ύX����B
	 * 
	 * @param image
	 *            ���݂̃A�C�R���ƈႤ�ꍇ�̂ݕύX����B
	 */
	public void replaceImage(Image image) {
		synchronized (INSTANCE_LOCK) {
			if (!image.equals(this.trayIcon.getImage())) {
				this.trayIcon.setImage(image);
			}
		}
	}

	/**
	 * �g���C�A�C�R�����쐬���A�V�X�e���g���C�ɒǉ�����B �A�C�R����Ŕ��������C�x���g�ɑ΂��郊�X�i�� targetFrame�ɐݒ肵�܂��B
	 * �����ł̃C�x���g�́A�g���C�E�A�C�R�����_�u���N���b�N���ꂽ���ɑΏۂ̃A�v���P�[�V���������o���܂��B
	 * 
	 * @param targetFrame
	 *            �Ώۂ̃A�v���P�[�V����
	 */
	public void createTray(JFrame targetFrame) {
		createTray(targetFrame, null, null);
	}

	/**
	 * �g���C�A�C�R�����쐬���A�V�X�e���g���C�ɒǉ�����B
	 * 
	 * @param targetFrame
	 *            �Ώۂ̃A�v���P�[�V����
	 * @param image
	 *            �g���C�A�C�R���ɕ\������C���[�W�摜�B null�̏ꍇ�AtargetFrame ����getIconImage()�Ŏ擾�ł���
	 *            �C���[�W�摜���g�p���܂��B
	 * @param menu
	 *            �^�X�N�g���C�ɕ\������PopupMenu�B null�̏ꍇ�A�f�t�H���g��PopupMenu��\�����܂��B
	 */
	public void createTray(final JFrame targetFrame, Image image, PopupMenu menu) {
		// �V�X�e���E�g���C���T�|�[�g����Ă��Ȃ���ΏI���B
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
	 * �^�X�N�g���C�̃A�C�R����ύX����B �����̂��_�ł��Ă���A�C�R����߂��ꍇ�́A
	 * {@link SystemTray#remove(TrayIcon)})�ō폜���Ȃ��� �ʖڂȂ悤�ł���B
	 * 
	 * @param image
	 *            ���݂̃A�C�R���ƈႤ�ꍇ�̂ݕύX����B
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
