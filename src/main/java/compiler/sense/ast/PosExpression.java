/**
 * 
 */
package compiler.sense.ast;

import compiler.typesystem.Type;

/**
 * 
 */
public class PosExpression extends ExpressionNode{

	private ArithmeticOperation operation;

	/**
	 * Constructor.
	 * @param addition
	 */
	public PosExpression(ArithmeticOperation op) {
		this.operation = op;
	}

	public Type getType(){
		return ((ExpressionNode)this.getChildren().get(0)).getType();
	}
	
	public ArithmeticOperation getOperation(){
		return operation;
	}

}
