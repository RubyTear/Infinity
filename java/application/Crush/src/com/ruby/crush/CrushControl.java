package com.ruby.crush;

import java.awt.Point;

public class CrushControl {
	private int[][] _arr;
	private byte[][] _flag;
	private int M;
	private int N;
	private boolean _crushingFlag;
	private boolean _validateOver;
	private int _score;

	public CrushControl() {
		this._score = 0;
		this.M = 10;
		this.N = 10;
		this._crushingFlag = false;
		this._validateOver = true;
		this._arr = new int[M][N];
		this._flag = new byte[M][N];
		initBlockNum();
	}

	public int[][] get_arr() {
		return _arr;
	}

	public byte[][] get_flag() {
		return _flag;
	}

	public int getM() {
		return M;
	}

	public int getN() {
		return N;
	}

	public void initFlag() {
		_flag = new byte[M][N];
	}

	public int get_score() {
		return _score;
	}

	public void set_score(int _score) {
		this._score = _score;
	}

	public boolean _validateOver() {
		return _validateOver;
	}

	private void initBlockNum() {
		for (int i = 0; i < 3; i++) {
			int rd = (int) Math.round(Math.random() * 3 + 1);
			int m = (int) Math.round(Math.random() * 9);
			int n = (int) Math.round(Math.random() * 9);
			if (_arr[m][n] == 0) {
				_arr[m][n] = rd;
			} else {
				i--;
			}
		}
	}

	public void generateBlock(int blockNum) {
		for (int i = 0; i < blockNum; i++) {
			int rd = (int) Math.round(Math.random() * 3 + 1);
			int m = (int) Math.round(Math.random() * 9);
			int n = (int) Math.round(Math.random() * 9);
			if (_arr[m][n] == 0) {
				_arr[m][n] = rd;
			} else {
				i--;
			}
		}
	}

	public boolean switchTheBlock(int from, int to) {
		int from_i = from / 10;
		int from_j = from % 10;
		int to_i = to / 10;
		int to_j = to % 10;
		if (validateMotion(new Point(from_i, from_j), new Point(to_i, to_j))) {
			int temp;
			temp = _arr[from_i][from_j];
			_arr[from_i][from_j] = _arr[to_i][to_j];
			_arr[to_i][to_j] = temp;
			return true;
		}
		return false;
	}

	private boolean validateMotion(Point from, Point to) {
		Point movepoint = new Point(from);
		boolean retrnVal = true;
		return retrnVal;
	}

	public boolean get_crushingFlag() {
		return _crushingFlag;
	}

	public void set_crushingFlag(boolean _crushedFlag) {
		this._crushingFlag = _crushedFlag;
	}

	public void crushTheBlock() {
		_validateOver = false;
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				if (j + 1 < N && j + 2 < N && j + 3 < N && j + 4 < N) {
					if (_arr[i][j] != 0 && (_arr[i][j] == _arr[i][j + 1] || _arr[i][j + 1] == 4)
							&& (_arr[i][j] == _arr[i][j + 2] || _arr[i][j + 2] == 4)
							&& (_arr[i][j] == _arr[i][j + 3] || _arr[i][j + 3] == 4)
							&& (_arr[i][j] == _arr[i][j + 4] || _arr[i][j + 4] == 4)) {
						_flag[i][j] = 1;
						_flag[i][j + 1] = 1;
						_flag[i][j + 2] = 1;
						_flag[i][j + 3] = 1;
						_flag[i][j + 4] = 1;
					}
					if (_arr[i][j] == 4 && _arr[i][j + 1] != 0 && _arr[i][j + 2] != 0 && _arr[i][j + 3] != 0
							&& _arr[i][j + 4] != 0) {
						int tempInt = 0;
						Boolean tempFlag = null;
						for (int k = 1; k < 5; k++) {
							if (_arr[i][j + k] != 4) {
								if (tempInt == 0) {
									tempFlag = true;
									tempInt = _arr[i][j + k];
								} else {
									if (_arr[i][j + k] == tempInt) {
										continue;
									} else {
										tempFlag = false;
										break;
									}
								}
							}
						}
						if (tempFlag != null && tempFlag) {
							_flag[i][j] = 1;
							_flag[i][j + 1] = 1;
							_flag[i][j + 2] = 1;
							_flag[i][j + 3] = 1;
							_flag[i][j + 4] = 1;
						}
					}
				}
				if (i + 1 < M && i + 2 < M && i + 3 < M && i + 4 < M) {
					if (_arr[i][j] != 0 && (_arr[i][j] == _arr[i + 1][j] || _arr[i + 1][j] == 4)
							&& (_arr[i][j] == _arr[i + 2][j] || _arr[i + 2][j] == 4)
							&& (_arr[i][j] == _arr[i + 3][j] || _arr[i + 3][j] == 4)
							&& (_arr[i][j] == _arr[i + 4][j] || _arr[i + 4][j] == 4)) {
						_flag[i][j] = 1;
						_flag[i + 1][j] = 1;
						_flag[i + 2][j] = 1;
						_flag[i + 3][j] = 1;
						_flag[i + 4][j] = 1;
					}
					if (_arr[i][j] == 4 && _arr[i + 1][j] != 0 && _arr[i + 2][j] != 0 && _arr[i + 3][j] != 0
							&& _arr[i + 4][j] != 0) {
						int tempInt = 0;
						Boolean tempFlag = null;
						for (int k = 1; k < 5; k++) {
							if (_arr[i + k][j] != 4) {
								if (tempInt == 0) {
									tempFlag = true;
									tempInt = _arr[i + k][j];
								} else {
									if (_arr[i + k][j] == tempInt) {
										continue;
									} else {
										tempFlag = false;
										break;
									}
								}
							}
						}
						if (tempFlag != null && tempFlag) {
							_flag[i][j] = 1;
							_flag[i + 1][j] = 1;
							_flag[i + 2][j] = 1;
							_flag[i + 3][j] = 1;
							_flag[i + 4][j] = 1;
						}
					}
				}
				if ((i + 1 < M && j + 1 < N) && (i + 2 < M && j + 2 < N) && (i + 3 < M && j + 3 < N)
						&& (i + 4 < M && j + 4 < N)) {
					if (_arr[i][j] != 0 && (_arr[i][j] == _arr[i + 1][j + 1] || _arr[i + 1][j + 1] == 4)
							&& (_arr[i][j] == _arr[i + 2][j + 2] || _arr[i + 2][j + 2] == 4)
							&& (_arr[i][j] == _arr[i + 3][j + 3] || _arr[i + 3][j + 3] == 4)
							&& (_arr[i][j] == _arr[i + 4][j + 4] || _arr[i + 4][j + 4] == 4)) {
						_flag[i][j] = 1;
						_flag[i + 1][j + 1] = 1;
						_flag[i + 2][j + 2] = 1;
						_flag[i + 3][j + 3] = 1;
						_flag[i + 4][j + 4] = 1;
					}
					if (_arr[i][j] == 4 && _arr[i + 1][j + 1] != 0 && _arr[i + 2][j + 2] != 0 && _arr[i + 3][j + 3] != 0
							&& _arr[i + 4][j + 4] != 0) {
						int tempInt = 0;
						Boolean tempFlag = null;
						for (int k = 1; k < 5; k++) {
							if (_arr[i + k][j + k] != 4) {
								if (tempInt == 0) {
									tempFlag = true;
									tempInt = _arr[i + k][j + k];
								} else {
									if (_arr[i + k][j + k] == tempInt) {
										continue;
									} else {
										tempFlag = false;
										break;
									}
								}
							}
						}
						if (tempFlag != null && tempFlag) {
							_flag[i][j] = 1;
							_flag[i + 1][j + 1] = 1;
							_flag[i + 2][j + 2] = 1;
							_flag[i + 3][j + 3] = 1;
							_flag[i + 4][j + 4] = 1;
						}
					}
				}
			}
		}
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				if (_flag[i][j] == 1) {
					_arr[i][j] = 0;
					_score += 20;
					if (!_crushingFlag) {
						_crushingFlag = true;
					}
				}
			}
		}
		_validateOver = true;
	}

	public boolean validateEndGame() {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				if (_arr[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public void showArrValues() {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				System.out.print(_arr[j][i] + " ");
			}
			System.out.println();
		}
		System.out.println("##########################");
	}
}
