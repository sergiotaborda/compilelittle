/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class WhileNode extends StatementNode {

	private BlockNode block;
	private ExpressionNode condition;

	/**
	 * @param astNode
	 */
	public void setBlock(BlockNode block) {
		this.block = block;
		this.add(block);
	}

	/**
	 * @param blockNode
	 */
	public void setCondition(ExpressionNode exp) {
		condition = exp;
		this.add(exp);
	}

}
