package com.ruby.crush;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

public class ShadowBlockMouseListener implements MouseListener {
	CrushPaintBlock _crushPaintBlock;
	JButton _shadowBlock;

	public ShadowBlockMouseListener(CrushPaintBlock crushPaintBlock, JButton shadowBlock) {
		this._crushPaintBlock = crushPaintBlock;
		this._shadowBlock = shadowBlock;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (this._crushPaintBlock.is_selectedFlag()) {
			Point shadowPoint = this._shadowBlock.getLocation();
			Point blockPoint = this._crushPaintBlock.get_selectedBlock().getLocation();
			int from = (blockPoint.x + 1) / this._crushPaintBlock.get_bottomPanel().get_tileSize() * 10
					+ (blockPoint.y + 1) / this._crushPaintBlock.get_bottomPanel().get_tileSize();
			int to = (shadowPoint.x - 2) / this._crushPaintBlock.get_bottomPanel().get_tileSize() * 10
					+ (shadowPoint.y - 2) / this._crushPaintBlock.get_bottomPanel().get_tileSize();
			if (this._crushPaintBlock.get_crushController().switchTheBlock(from, to)) {
				this._shadowBlock.setLocation(blockPoint.x + 2 + 1, blockPoint.y + 2 + 1);
			}
			this._crushPaintBlock.get_selectedBlock().setText("");
			this._crushPaintBlock.set_selectedFlag(false);
			this._crushPaintBlock.set_selectedBlock(null);
			this._crushPaintBlock.set_gameTurn(this._crushPaintBlock.get_gameTurn() + 1);
			this._crushPaintBlock.set_gameTurnChangeFlag(true);
		} else { // 何もしない } }
		}
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
