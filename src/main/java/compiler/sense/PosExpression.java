/**
 * 
 */
package compiler.sense;

import compiler.sense.ArithmeticNode.Operation;
import compiler.syntax.AstNode;

/**
 * 
 */
public class PosExpression extends ExpressionNode{

	private Operation operation;

	/**
	 * Constructor.
	 * @param addition
	 */
	public PosExpression(Operation op) {
		this.operation = op;
	}

}
