/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class VariableNameNode extends AstNode {

	private String name;
	private ExpressionNode initValue;

	/**
	 * @param object
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param string
	 */
	public void setInitialValue(ExpressionNode initValue) {
		this.initValue = initValue;
	}

}
