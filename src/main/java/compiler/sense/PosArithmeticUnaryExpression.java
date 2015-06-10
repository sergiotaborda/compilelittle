/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;

/**
 * 
 */
public class PosArithmeticUnaryExpression extends ExpressionNode {

	private ArithmeticOperation operation;

	/**
	 * Constructor.
	 * @param resolveBooleanOperation
	 */
	public PosArithmeticUnaryExpression(ArithmeticOperation operation) {
		this.operation = operation;
	}

	/**
	 * Obtains {@link ArithmeticOperation}.
	 * @return the operation
	 */
	public ArithmeticOperation getOperation() {
		return operation;
	}

	/**
	 * @param returningType
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	
}
