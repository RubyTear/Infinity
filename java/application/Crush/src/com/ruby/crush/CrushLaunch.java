package com.ruby.crush;

import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public class CrushLaunch implements Runnable {
	private boolean _runGame = false;
	private CruchWindow _window;
	private CrushPaint _crushPaint;

	public CrushLaunch(CruchWindow window, CrushPaint crushPaint) {
		this._runGame = true;
		this._window = window;
		this._crushPaint = crushPaint;
	}

	private void CrushTheBlockValidation() {
		synchronized (this) {
			try {
				_crushPaint.get_crushController().crushTheBlock();
				if (_crushPaint.get_crushController().get_crushingFlag()) {
					_crushPaint._crushPaintBlock
							.crushReGenarateShadowBlock(_crushPaint.get_crushController().get_flag());
					_crushPaint.get_crushController().set_crushingFlag(false);
					_crushPaint.get_crushController().initFlag();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (_runGame) {
			synchronized (this) {
				try {
					_window.validate();
					_window.repaint();
					CrushTheBlockValidation();
					_crushPaint.set_isGG(_crushPaint.get_crushController().validateEndGame());
					if (_crushPaint._isGG()) {
						_runGame = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] arg) {
		JPanel bottom = new JPanel();
		bottom.setLayout(new OverlayLayout(bottom));
		CrushPaint _crushPanel = new CrushPaint();
		bottom.add(_crushPanel._crushPaintBlock);
		bottom.add(_crushPanel);

		CruchWindow window = new CruchWindow(bottom);
		CrushLaunch launcher = new CrushLaunch(window, _crushPanel);
		launcher.run();
	}
}
