package com.ruby.crush.test;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class sample extends JFrame {
	public static void main(String args[]) {
		sample frame = new sample("タイトル");
		frame.setVisible(true);
	}

	sample(String title) {
		setTitle(title);
		setBounds(100, 100, 300, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel p = new JPanel();

		JButton button1 = new JButton();
		JButton button2 = new JButton("確認");
		JButton button3 = new JButton();
		button3.setText("キャンセル");
		button1.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("YELLOW.png")));
		p.add(button1);
		p.add(button2);
		p.add(button3);

		Container contentPane = getContentPane();
		contentPane.add(p, BorderLayout.CENTER);
	}
}
