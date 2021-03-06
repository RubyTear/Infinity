package com.ruby.crush;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

public class CrushPaintBlockMouseListener implements MouseListener {
	private CrushPaintBlock _crushPaintBlock;
	private JLabel _block;

	public CrushPaintBlockMouseListener(CrushPaintBlock crushPaintBlock) {
		this._crushPaintBlock = crushPaintBlock;
	}

	public CrushPaintBlockMouseListener() {
	}

	public CrushPaintBlockMouseListener(CrushPaintBlock crushPaintBlock, JLabel block) {
		this._crushPaintBlock = crushPaintBlock;
		this._block = block;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!this._crushPaintBlock.is_selectedFlag()) {
			if (this._block.getText() == null || this._block.getText().isEmpty()) {
				this._block.setText("SELECTED");
				this._block.setLocation(this._block.getLocation().x - 1, this._block.getLocation().y - 1);
				this._crushPaintBlock.set_selectedFlag(true);
				this._crushPaintBlock.set_selectedBlock(this._block);
			}
		} else {
			if (this._block.getText() == null || this._block.getText().isEmpty()) {
				JLabel selectedBall = this._crushPaintBlock.get_selectedBlock();
				selectedBall.setText("");
				selectedBall.setLocation(selectedBall.getLocation().x + 1, selectedBall.getLocation().y + 1);
				this._block.setText("SELECTED");
				this._block.setLocation(this._block.getLocation().x - 1, this._block.getLocation().y - 1);
				this._crushPaintBlock.set_selectedFlag(true);
				this._crushPaintBlock.set_selectedBlock(this._block);
			} else if ("SELECTED".equals(this._block.getText())) {
				this._block.setText("");
				this._block.setLocation(this._block.getLocation().x + 1, this._block.getLocation().y + 1);
				this._crushPaintBlock.set_selectedFlag(false);
				this._crushPaintBlock.set_selectedBlock(null);
			}
		}
	}

	private void exChangeBall() {
		Point thisBlock = this._block.getLocation();
		Point blockPoint = this._crushPaintBlock.get_selectedBlock().getLocation();
		int from = (blockPoint.x + 1) / this._crushPaintBlock.get_bottomPanel().get_tileSize() * 10
				+ (blockPoint.y + 1) / this._crushPaintBlock.get_bottomPanel().get_tileSize();
		int to = (thisBlock.x) / this._crushPaintBlock.get_bottomPanel().get_tileSize() * 10
				+ (thisBlock.y) / this._crushPaintBlock.get_bottomPanel().get_tileSize();
		this._crushPaintBlock.get_crushController().switchTheBlock(from, to);
		this._crushPaintBlock.get_selectedBlock().setText("");
		this._crushPaintBlock.set_selectedFlag(false);
		this._crushPaintBlock.set_selectedBlock(null);
		this._crushPaintBlock.set_gameTurn(this._crushPaintBlock.get_gameTurn() + 1);
		this._crushPaintBlock.set_gameTurnChangeFlag(true);
	}

	@Override
	public void mousePressed(MouseEvent e) { // TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseReleased(MouseEvent e) { // TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseEntered(MouseEvent e) { // TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseExited(MouseEvent e) { // TODO 自動生成されたメソッド・スタブ
	}
}
