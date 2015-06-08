/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class CatchOptionNode extends SenseAstNode {

	private AstNode exceptions;
	private BlockNode instructions;

	/**
	 * @param astNode
	 */
	public void setExceptions(AstNode node) {
		this.exceptions = node;
		this.add(node);
	}

	/**
	 * @param astNode
	 */
	public void setInstructions(BlockNode node) {
		this.instructions = node;
		this.add(node);
	}

}
