/**
 * 
 */
package compiler.sense;

import compiler.sense.ArithmeticNode.Operation;

/**
 * 
 */
public class PosArithmeticUnaryExpression extends ExpressionNode {

	private Operation operation;

	/**
	 * Constructor.
	 * @param resolveBooleanOperation
	 */
	public PosArithmeticUnaryExpression(Operation operation) {
		this.operation = operation;
	}

}
