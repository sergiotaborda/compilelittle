/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class SwitchOption extends SenseAstNode {

	private ExpressionNode value;
	private AstNode actions;

	/**
	 * @param expressionNode
	 */
	public void setValue(ExpressionNode expressionNode) {
		this.value = expressionNode;
	}

	/**
	 * @param astNode
	 */
	public void setActions(AstNode node) {
		this.actions = node;
	}

}
