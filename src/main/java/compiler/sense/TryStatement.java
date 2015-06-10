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
		this.add(blockNode);
	}

	/**
	 * @param expressionNode
	 */
	public void setResource(ExpressionNode expressionNode) {
		this.resource = expressionNode;
		this.add(expressionNode);
	}

	/**
	 * @param blockNode
	 */
	public void setFinally(BlockNode blockNode) {
		this.finalInstructions = blockNode;
		this.add(finalInstructions);
	}

	/**
	 * @param catchOptionsNode
	 */
	public void setCatchOptions(CatchOptionsNode catchOptionsNode) {
		this.catchOptions = catchOptionsNode;
		this.add(catchOptionsNode);
	}

}
