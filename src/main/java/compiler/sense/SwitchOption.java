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
	private BlockNode actions;

	/**
	 * @param expressionNode
	 */
	public void setValue(ExpressionNode expressionNode) {
		this.value = expressionNode;
		this.add(expressionNode);
	}

	/**
	 * @param astNode
	 */
	public void setActions(BlockNode node) {
		this.actions = node;
		this.add(node);
	}

	/**
	 * Obtains {@link ExpressionNode}.
	 * @return the value
	 */
	public ExpressionNode getValue() {
		return value;
	}

	/**
	 * Obtains {@link AstNode}.
	 * @return the actions
	 */
	public BlockNode getActions() {
		return actions;
	}

}
