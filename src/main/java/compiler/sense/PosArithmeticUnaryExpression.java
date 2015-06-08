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

	public Type getType(){
		return ((ExpressionNode)this.getChildren().get(0)).getType();
	}
}
