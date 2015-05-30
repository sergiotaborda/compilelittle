/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class DecisionNode extends AstNode {

	private ExpressionNode condition;
	private BlockNode trueBlock;
	private BlockNode falseBlock;

	/**
	 * @param expressionNode
	 */
	public void setCondition(ExpressionNode condition) {
		this.condition = condition;
		this.add(condition);
	}

	/**
	 * @param blockNode
	 */
	public void setTruePath(BlockNode trueBlock) {
		this.trueBlock = trueBlock;
	}

	/**
	 * @param blockNode
	 */
	public void setFalsePath(BlockNode falseBlock) {
		this.falseBlock = falseBlock;
	}

}
