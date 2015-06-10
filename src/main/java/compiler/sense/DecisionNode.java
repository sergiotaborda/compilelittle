/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class DecisionNode extends StatementNode implements ConditionalStatement {

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
		this.add(trueBlock);
	}

	/**
	 * @param blockNode
	 */
	public void setFalsePath(BlockNode falseBlock) {
		this.falseBlock = falseBlock;
		this.add(falseBlock);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExpressionNode getCondition() {
		return condition;
	}
}
