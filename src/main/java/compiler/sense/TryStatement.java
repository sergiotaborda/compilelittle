/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class TryStatement extends SenseAstNode {

	private BlockNode instructions;
	private ExpressionNode resource;
	private BlockNode finalInstructions;
	private CatchOptionsNode catchOptions;

	/**
	 * @param blockNode
	 */
	public void setInstructions(BlockNode blockNode) {
		this.instructions = blockNode;
	}

	/**
	 * @param expressionNode
	 */
	public void setResource(ExpressionNode expressionNode) {
		this.resource = expressionNode;
	}

	/**
	 * @param blockNode
	 */
	public void setFinally(BlockNode blockNode) {
		this.finalInstructions = blockNode;
	}

	/**
	 * @param catchOptionsNode
	 */
	public void setCatchOptions(CatchOptionsNode catchOptionsNode) {
		this.catchOptions = catchOptionsNode;
	}

}
