package com.ruby.crush;

public enum Block {
	YELLOW(1), PINK(2), BLUE(3), ALL(4);
	private int num;

	private Block(int num) {
		this.num = num;
	}

	public int getValue() {
		return this.num;
	}

	public static Block getType(final int id) {
		Block[] BlockTypes = Block.values();
		for (Block type : BlockTypes) {
			if (type.getValue() == id) {
				return type;
			}
		}
		return null;
	}
}
