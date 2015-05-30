/**
 * 
 */
package compiler.parser;

import compiler.syntax.AstNode;

/**
 * 
 */
public class NumericNode extends AstNode {

	private String numberValue;

	/**
	 * Constructor.
	 * @param string
	 */
	public NumericNode(String numberValue) {
		this.numberValue = numberValue;
	}

	public String getNumberValue() {
		return numberValue;
	}


}
