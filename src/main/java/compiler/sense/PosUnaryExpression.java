/**
 * 
 */
package compiler.sense;

import compiler.sense.BooleanOperatorNode.Operation;

/**
 * 
 */
public class PosUnaryExpression extends ExpressionNode {

	private Operation operation;

	/**
	 * Constructor.
	 * @param resolveBooleanOperation
	 */
	public PosUnaryExpression(Operation operation) {
		this.operation = operation;
	}

}
