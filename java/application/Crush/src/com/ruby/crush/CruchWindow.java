package com.ruby.crush;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CruchWindow extends JFrame {
	/** * */
	private static final long serialVersionUID = 1L;
	private static final int _windowWidth = 530;
	private static final int _windowHeight = 450;

	public CruchWindow(JPanel _panel) {
		this.setTitle("Cruch 1.0");
		this.setSize(_windowWidth, _windowHeight);
		Toolkit _toolKit = Toolkit.getDefaultToolkit();
		Dimension _screenSize = _toolKit.getScreenSize();
		final int _screenWidth = _screenSize.width;
		final int _screenHeight = _screenSize.height;
		this.setLocation((_screenWidth - this.getWidth()) / 2, (_screenHeight - this.getHeight()) / 2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		Container container = this.getContentPane();
		container.add(_panel);
	}
}
