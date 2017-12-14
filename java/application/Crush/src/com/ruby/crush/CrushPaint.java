package com.ruby.crush;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CrushPaint extends JPanel {
	private static final long serialVersionUID = 1L;
	private CrushPaint _gameMain;
	private JLabel _showLabel;
	private JButton _showScore;
	private JButton _startButton;
	private boolean _isGG;
	private boolean _isStart;
	private int _x;
	private int _y;
	private int _panelWidth;
	private int _panelHeight;
	private int _tileSize;
	private CrushControl _crushController;
	CrushPaintBlock _crushPaintBlock;

	public CrushPaint() {
		init();
		_gameMain = this;
	}

	public int get_x() {
		return _x;
	}

	public int get_y() {
		return _y;
	}

	public int get_panelWidth() {
		return _panelWidth;
	}

	public int get_panelHeight() {
		return _panelHeight;
	}

	public int get_tileSize() {
		return _tileSize;
	}

	public CrushControl get_crushController() {
		return _crushController;
	}

	public boolean _isGG() {
		return _isGG;
	}

	public void set_isGG(boolean _isGG) {
		this._isGG = _isGG;
	}

	private void init() {
		this._x = 0;
		this._y = 0 + 10;
		this._panelWidth = 400;
		this._panelHeight = 400 + 10;
		this._tileSize = 40;
		_crushController = new CrushControl();
		_crushPaintBlock = new CrushPaintBlock(this, _crushController);
		this._isStart = true;
		this._isGG = false;
		this.setLayout(null);
		_showLabel = new JLabel();
		_showLabel.setFont(new Font("Dialog", 1, 20));
		_showLabel.setText("Score:");
		_showLabel.setForeground(Color.BLUE);
		_showLabel.setLocation(420, 100);
		_showLabel.setBackground(new Color(240, 240, 240));
		_showLabel.setSize(80, 60);
		this.add(_showLabel);
		_showScore = new JButton("0");
		_showScore.setSize(50, 60);
		_showScore.setLocation(480, 100);
		_showScore.setBackground(new Color(240, 240, 240));
		_showScore.setFont(new Font("Dialog", 1, 20));
		_showScore.setBorder(null);
		_showScore.setEnabled(false);
		this.add(_showScore);
		_startButton = new JButton();
		_startButton.setText("Start");
		_startButton.setLocation(420, 250);
		_startButton.setSize(_startButton.getPreferredSize());
		_startButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) { // TODO 自動生成されたメソッド・スタブ
			}

			@Override
			public void mousePressed(MouseEvent e) { // TODO 自動生成されたメソッド・スタブ
			}

			@Override
			public void mouseExited(MouseEvent e) { // TODO 自動生成されたメソッド・スタブ
			}

			@Override
			public void mouseEntered(MouseEvent e) { // TODO 自動生成されたメソッド・スタブ
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				_isStart = true;
				if (_isGG) {
					_gameMain._crushPaintBlock.removeAll();
					_gameMain.init();
				}
			}
		});
		this.add(_startButton);
		this.requestFocus();
	}

	@Override
	protected void paintComponent(Graphics pen) {
		try {
			super.paintComponent(pen);
			CreatGameInit(pen);
			if (_isGG) {
				if (!this._crushPaintBlock._isGG()) {
					this._crushPaintBlock.set_isGG(true);
				}
				return;
			}
			if (_isStart) {
				createGame();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createGame() {
		if (!this._crushPaintBlock._isStart()) {
			this._crushPaintBlock.set_isStart(true);
		}
		this._showScore.setText(String.valueOf(_crushController.get_score()));
	}

	private void CreatGameInit(Graphics pen) {
		pen.setColor(Color.BLACK);
		pen.drawRect(_x, _y, _panelWidth, _panelHeight);
		pen.setColor(Color.WHITE);
		pen.fillRect(_x + 1, _y + 1, _panelWidth - 1, _panelHeight - 1);
		pen.setColor(Color.GRAY);
		for (int i = 1; i < this._panelWidth / this._tileSize; ++i) {
			pen.drawLine(this._x + i * _tileSize, this._y, this._x + i * _tileSize, this._y + this._panelHeight);
		}
		for (int i = 1; i < this._panelHeight / this._tileSize; ++i) {
			pen.drawLine(this._x, this._y + i * _tileSize, this._x + this._panelWidth, this._y + i * this._tileSize);
		}
	}
}
