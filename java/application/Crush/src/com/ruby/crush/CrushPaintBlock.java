package com.ruby.crush;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CrushPaintBlock extends JPanel {
	private CrushPaint _bottomPanel;
	private int _massX_Num;
	private int _massY_Num;
	private int _gameTurn;
	private CrushControl _crushController;
	private boolean _gameTurnChangeFlag;
	private boolean _selectedFlag;
	private JLabel _selectedBlock;
	private boolean _isGG;
	private boolean _isStart;
	private boolean _isReGanaProcessOver;
	private Map<Integer, Block> _blockMap = new HashMap<Integer, Block>();
	/** * */
	private static final long serialVersionUID = 1L;

	public CrushPaintBlock(CrushPaint panel, CrushControl crushController) {
		this._isGG = false;
		this._isStart = false;
		this._isReGanaProcessOver = true;
		this._gameTurn = 0;
		this._gameTurnChangeFlag = false;
		this._selectedFlag = false;
		this._crushController = crushController;
		this._bottomPanel = panel;
		// this.setBackground(Color.RED);
		this.setLayout(null);
		for (int i = 0; i < _bottomPanel.get_panelWidth() / _bottomPanel.get_tileSize(); i++) {
			for (int j = 0; j < _bottomPanel.get_panelHeight() / _bottomPanel.get_tileSize(); j++) {
				generateShadowBlock(i, j);
			}
		}
		this.setPreferredSize(new Dimension(_bottomPanel.get_tileSize(), _bottomPanel.get_tileSize()));
		this.setOpaque(false);
	}

	public CrushPaint get_bottomPanel() {
		return this._bottomPanel;
	}

	public CrushControl get_crushController() {
		return _crushController;
	}

	public void set_crushController(CrushControl _crushController) {
		this._crushController = _crushController;
	}

	public boolean is_selectedFlag() {
		return _selectedFlag;
	}

	public void set_selectedFlag(boolean _selectedFlag) {
		this._selectedFlag = _selectedFlag;
	}

	public JLabel get_selectedBlock() {
		return _selectedBlock;
	}

	public void set_selectedBlock(JLabel _selectedBlock) {
		this._selectedBlock = _selectedBlock;
	}

	public boolean is_gameTurnChangeFlag() {
		return _gameTurnChangeFlag;
	}

	public void set_gameTurnChangeFlag(boolean _gameTurnChangeFlag) {
		this._gameTurnChangeFlag = _gameTurnChangeFlag;
	}

	public int get_gameTurn() {
		return _gameTurn;
	}

	public void set_gameTurn(int _gameTurn) {
		this._gameTurn = _gameTurn;
	}

	public Map<Integer, Block> get_blockMap() {
		return _blockMap;
	}

	public boolean _isGG() {
		return _isGG;
	}

	public void set_isGG(boolean _isGG) {
		this._isGG = _isGG;
	}

	public boolean _isStart() {
		return _isStart;
	}

	public void set_isStart(boolean _isStart) {
		this._isStart = _isStart;
	}

	public void setMassXY(int x, int y) {
		this._massX_Num = x;
		this._massY_Num = y;
	}

	public Block getBlockbyKey(int arrX, int arrY) {
		Integer arr = arrX * 10 + arrY;
		return this._blockMap.get(arr) != null ? this._blockMap.get(arr) : null;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			if (_isGG) {
				return;
			}
			if (_isStart) {
				gameStart();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void gameStart() {
		if (_gameTurn == 0) {
			_gameTurn++;
			for (int i = 0; i < _crushController.getM(); i++) {
				for (int j = 0; j < _crushController.getN(); j++) {
					if (_crushController.get_arr()[i][j] != 0) {
						this.setMassXY(i, j);
						Integer arrXY = i * 10 + j;
						_blockMap.put(arrXY, Block.getType(_crushController.get_arr()[i][j]));
						setBlock2Panel();
					}
				}
			}
			System.out.println("#########");
			_crushController.showArrValues();
		} else if (_gameTurnChangeFlag && _gameTurn >= 1 && _crushController._validateOver() && _isReGanaProcessOver) {
			_crushController.generateBlock(2);
			removeOriginalBlock();
			int count_ball = 0;
			for (int i = 0; i < _crushController.getM(); i++) {
				for (int j = 0; j < _crushController.getN(); j++) {
					if (_crushController.get_arr()[i][j] != 0) {
						this.setMassXY(i, j);
						Integer arrXY = i * 10 + j;
						_blockMap.put(arrXY, Block.getType(_crushController.get_arr()[i][j]));
						setBlock2Panel();
						count_ball++;
					}
				}
			}
			this._gameTurnChangeFlag = false;
			System.out.println("ball:" + count_ball);
			int count = 0;
			for (int i = 0; i < this.getComponentCount(); i++) {
				if (this.getComponent(i) instanceof JButton) {
					count++;
				}
			}
			System.out.println("blank:" + count);
			_crushController.showArrValues();
			System.out.println("#########");
		}
	}

	private void setBlock2Panel() {
		if (this.getBlockbyKey(_massX_Num, _massY_Num) == null) {
			return;
		}
		JLabel label = new JLabel();
		switch (this.getBlockbyKey(_massX_Num, _massY_Num)) {
		case BLUE:
			label.setIcon(new ImageIcon(CruchWindow.class.getClassLoader().getResource("BLUE.png")));
			break;
		case PINK:
			label.setIcon(new ImageIcon(CruchWindow.class.getClassLoader().getResource("PINK.png")));
			break;
		case YELLOW:
			label.setIcon(new ImageIcon(CruchWindow.class.getClassLoader().getResource("YELLOW.png")));
			break;
		case ALL:
			label.setIcon(new ImageIcon(CruchWindow.class.getClassLoader().getResource("ALL.png")));
			break;
		default:
			return;
		}
		label.setLocation(_massX_Num * _bottomPanel.get_tileSize(), _massY_Num * _bottomPanel.get_tileSize() + 10);
		label.setSize(label.getPreferredSize());
		label.addMouseListener(new CrushPaintBlockMouseListener(this, label));
		// for (int i = 0; i < this.getComponentCount(); i++) {
		// for (int j = 0; j < this.getComponents().length; j++) {
		// Component blockPanelComp = this.getComponents()[j];
		// if (blockPanelComp instanceof JButton) {
		// if (_massX_Num == (blockPanelComp.getLocation().x - 2) /
		// _bottomPanel.get_tileSize()
		// && _massY_Num == (blockPanelComp.getLocation().y - 2) /
		// _bottomPanel.get_tileSize()) {
		// this.remove(blockPanelComp);
		// System.out.println("one gone!!");
		// }
		// }
		// }
		// }
		if (this.getComponentAt(_massX_Num * _bottomPanel.get_tileSize() + 2,
				_massY_Num * _bottomPanel.get_tileSize() + 10 + 2) instanceof JButton) {
			JButton shadowblock = (JButton) this.getComponentAt(_massX_Num * _bottomPanel.get_tileSize() + 2,
					_massY_Num * _bottomPanel.get_tileSize() + 10 + 2);
			this.remove(shadowblock);
		}
		this.add(label);

	}

	public void crushReGenarateShadowBlock(byte[][] flag) {
		_isReGanaProcessOver = false;
		for (int k = 0; k < flag.length; k++) {
			byte[] bs = flag[k];
			for (int l = 0; l < bs.length; l++) {
				if (flag[k][l] == 1) {
					generateShadowBlock(k, l);
				}
			}
		}
		_isReGanaProcessOver = true;
	}

	private void generateShadowBlock(int x, int y) {
		JButton shadowBlock = new JButton("");
		shadowBlock.setSize(36, 36);
		shadowBlock.setLocation(_bottomPanel.get_x() + 2 + x * _bottomPanel.get_tileSize(),
				_bottomPanel.get_y() + 2 + y * _bottomPanel.get_tileSize());
		shadowBlock.setBackground(new Color(255, 255, 255, 100));
		shadowBlock.setBorder(null);
		shadowBlock.addMouseListener(new ShadowBlockMouseListener(this, shadowBlock));
		this.add(shadowBlock);
	}

	private void removeOriginalBlock() {
		Component[] comps = this.getComponents();
		for (int i = 0; i < comps.length; i++) {
			Component component = comps[i];
			if (component instanceof JLabel) {
				Point block_XY = component.getLocation();
				this.remove(component);
				// generateShadowBlock(block_XY.x + 2, block_XY.y + 2);
			}
		}
	}
}
