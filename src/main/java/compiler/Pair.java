package compiler;

import compiler.parser.Production;

class Pair {

    Production left;
	Production right;

	public Pair(Production left, Production right) {
		this.left = left;
		this.right = right;
	}

}
