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
 * �g�ѓd�b�̕\���E�B���h�E��\���B
 * 
 * @author Acroquest
 * 
 */
public class PhoneFrame extends JFrame {
	/** �E�B���h�E�̃^�C�g��������B */
	private static final String FRAME_TITLE = "HandyPhone (CORBA)";

	/** �E�B���h�E�̍ŏ��T�C�Y�B */
	private static final Dimension FRAME_MINIMUM_SIZE = new Dimension(500, 350);

	/** �{�^���̃f�t�H���g�T�C�Y�B */
	private static final Dimension BUTTON_PERFORMED_SIZE = new Dimension(86, 27);

	/** �t�B�[���h������������Ă��邩���f����t���O�B */
	private boolean isFieldInitialized_;

	/** �����̖��O��ݒ肷��e�L�X�g�t�B�[���h�B */
	private JTextField ownNameText_;

	/** �l�[�~���O�T�[�r�X�̃z�X�g��ݒ肷��e�L�X�g�t�B�[���h�B */
	private JTextField namingServiceHostText_;

	/** �l�[�~���O�T�[�r�X�̃|�[�g�ԍ���ݒ肷��e�L�X�g�t�B�[���h�B */
	private JTextField namingServicePortText_;

	/** �ڑ���̖��O��ݒ肷��e�L�X�g�t�B�[���h�B */
	private JTextField connectNameText_;

	/** ���M���b�Z�[�W����͂���e�L�X�g�t�B�[���h�B */
	private JTextField messageText_;

	/** ����M���b�Z�[�W��\������e�L�X�g�G���A�B */
	private JTextArea displayArea_;

	/** �\�P�b�g��M�҂���Ԃ̐ݒ���s���g�O���{�^���B */
	private JToggleButton powerTButton_;

	/** �ڑ���Ԃ̐ݒ���s���g�O���{�^���B */
	private JToggleButton connectTButton_;

	/** ���b�Z�[�W�𑗐M����{�^���B */
	private JButton sendButton_;

	/**
	 * �R���X�g���N�^�B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ�
	 */
	public PhoneFrame(PhoneController controller) {
		// �t�B�[���h�̏�����
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
	 * �\���E�B���h�E�ɕ������ǉ�����B
	 * 
	 * @param text
	 *            �ǉ����镶����
	 */
	public synchronized void appendText(String text) {
		this.displayArea_.append(text + "\n");
	}

	/**
	 * �G���[�_�C�A���O��\������B
	 * 
	 * @param errMsg
	 *            �G���[���b�Z�[�W
	 */
	public synchronized void showErrorDialog(String errMsg) {
		System.err.println(errMsg);
		this.appendText(errMsg);
	}

	/**
	 * �g�ѓd�b�E�B���h�E�̕`����X�e�[�^�X�ɍ��킹�čX�V����B
	 * 
	 * @param status
	 *            �ω���̃X�e�[�^�X
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

		String ownPortLabel = "�d��";
		String connectionLabel = "���ڑ�";

		switch (status) {
		case POWER_OFF: // �d���������Ă��Ȃ����
		{
			isOwnNameTextEditable = true;
			isNamingServiceTextEditable = true;
			this.displayArea_.setText("");
			this.connectNameText_.setText("");

			break;
		}
		case NO_CONNECTION: // �ڑ����Ȃ����
		{
			isOwnPortTButtonSelected = true;
			isDisplayEnabled = true;
			isConnectionTextEditable = true;
			isConnectionTButtonEnabled = true;
			this.messageText_.setText("");

			ownPortLabel = "�d��OFF";
			connectionLabel = "�ڑ��J�n";

			break;
		}
		case CONNECTION: // �ڑ����̏��
		{
			isOwnPortTButtonSelected = true;
			isDisplayEnabled = true;
			isConnectionTButtonEnabled = true;
			isConnectionTButtonSelected = true;
			isMessageTextEditable = true;
			isMessageButtonEnabled = true;

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
		this.ownNameText_.setEditable(isOwnNameTextEditable);
		this.namingServiceHostText_.setEditable(isNamingServiceTextEditable);
		this.namingServicePortText_.setEditable(isNamingServiceTextEditable);
		this.powerTButton_.setSelected(isOwnPortTButtonSelected);
		this.powerTButton_.setText(ownPortLabel);

		this.displayArea_.setEnabled(isDisplayEnabled);

		// �ڑ��ݒ�
		this.connectNameText_.setEditable(isConnectionTextEditable);
		this.connectTButton_.setSelected(isConnectionTButtonSelected);
		this.connectTButton_.setEnabled(isConnectionTButtonEnabled);
		this.connectTButton_.setText(connectionLabel);

		// ���M�ݒ�
		this.messageText_.setEditable(isMessageTextEditable);
		this.sendButton_.setEnabled(isMessageButtonEnabled);
	}

	/**
	 * �����̖��O�Ƃ��Ďw�肳�ꂽ�������Ԃ��B
	 * 
	 * @return �����̖��O
	 */
	public String getOwnName() {
		String ownName = this.ownNameText_.getText();
		return ownName;
	}

	/**
	 * �l�[�~���O�T�[�r�X�̃z�X�g�Ƃ��Ďw�肳�ꂽ�������Ԃ��B
	 * 
	 * @return �l�[�~���O�T�[�r�X�̃z�X�g���͗��̕�����
	 */
	public String getNamingServiceHost() {
		String host = this.namingServiceHostText_.getText();
		return host;
	}

	/**
	 * �l�[�~���O�T�[�r�X�̃|�[�g�Ƃ��Ďw�肳�ꂽ�������Ԃ��B
	 * 
	 * @return �l�[�~���O�T�[�r�X�̃|�[�g���͗��̕�����
	 */
	public String getNamingServicePortStr() {
		String portStr = this.namingServicePortText_.getText();
		return portStr;
	}

	/**
	 * �ڑ���Ƃ��Ďw�肳�ꂽ���O�̕������Ԃ��B
	 * 
	 * @return �ڑ���z�X�g�̕�����
	 */
	public String getConnectName() {
		String otherNameStr = this.connectNameText_.getText();
		return otherNameStr;
	}

	/**
	 * �ڑ���Ƃ��Ďw�肳�ꂽ���O�̕������ݒ肷��B
	 * 
	 * @param connectName
	 *            �ڑ���z�X�g�̕�����
	 */
	public void setConnectName(String connectName) {
		this.connectNameText_.setText(connectName);
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

		this.powerTButton_ = new JToggleButton("�d��ON");
		this.connectTButton_ = new JToggleButton("���ڑ�");
		this.sendButton_ = new JButton("���M");

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
	 * �A�N�V�������X�i��ݒ肷��B
	 * 
	 * @param controller
	 *            �g�ѓd�b�A�v���Ǘ�
	 */
	private void setActionListener(PhoneController controller) {
		ActionListener listener = new PhoneListener(controller);
		this.powerTButton_.addActionListener(listener);
		this.connectTButton_.addActionListener(listener);
		this.sendButton_.addActionListener(listener);
	}

	/**
	 * �g�ѓd�b�E�B���h�E�̏����ݒ����͂���p�l�����쐬����B
	 * 
	 * @param nameText
	 *            �����̖��O����͂���t�B�[���h
	 * @param hostText
	 *            �l�[�~���O�T�[�r�X�̃z�X�g���̓t�B�[���h
	 * @param portText
	 *            �l�[�~���O�T�[�r�X�̃|�[�g�ԍ����̓t�B�[���h
	 * @param powerButton
	 *            �ڑ��҂��ݒ�{�^��
	 * @return ���|�[�g�ԍ����̓p�l��
	 */
	private JPanel createNorthPanel(JTextField nameText, JTextField hostText,
			JTextField portText, AbstractButton powerButton) {
		JPanel returnPanel = new JPanel();
		BorderLayout borderLayout = new BorderLayout();
		returnPanel.setLayout(borderLayout);
		JLabel hostLabel = new JLabel("HOST");
		JLabel portLabel = new JLabel("PORT");
		JLabel nameLabel = new JLabel("���O");

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
	 * �g�ѓd�b�E�B���h�E�̕\����ʕt���p�l�����쐬����B
	 * 
	 * @param displayArea
	 *            �\�����
	 * @return �\����ʕt���p�l��
	 */
	private JPanel createCentorPanel(JTextArea displayArea) {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(displayArea);
		returnPanel.add(scrollPane, BorderLayout.CENTER);

		return returnPanel;
	}

	/**
	 * �g�ѓd�b�E�B���h�E�̑��M�ݒ�p�l�����쐬����B
	 * 
	 * @param otherNameText
	 *            ���M�於�O����͂���e�L�X�g�t�B�[���h
	 * @param connectButton
	 *            �ڑ��{�^��
	 * @param messageText
	 *            ���M���b�Z�[�W����͂���e�L�X�g�t�B�[���h
	 * @param sendButton
	 *            ���M�{�^��
	 * @return ���M�ݒ�p�l��
	 */
	private JPanel createSouthPanel(JTextField otherNameText,
			AbstractButton connectButton, JTextField messageText,
			AbstractButton sendButton) {
		// ���M��ݒ�p�l���̐���
		JPanel connectPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		connectPanel.setLayout(flowLayout);

		JLabel hostLabel = new JLabel("�ڑ���");

		connectPanel.add(hostLabel);
		connectPanel.add(otherNameText);
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
}
