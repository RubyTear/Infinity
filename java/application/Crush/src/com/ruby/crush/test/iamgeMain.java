package com.ruby.crush.test;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

public class iamgeMain {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		JButton button = new JButton();
		button.setText("meilan");
		button.setSize(button.getPreferredSize());
		// button.setIcon(new
		// ImageIcon(CruchWindow.class.getClassLoader().getResource("YELLOW.png")));
		bottom.add(button);
		bottom.setBackground(Color.RED);
		bottom.setVisible(true);
		new iconImage(bottom);
	}

}
