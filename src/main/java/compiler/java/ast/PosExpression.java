/**
 * 
 */
package compiler.java.ast;

import compiler.typesystem.Type;

/**
 * 
 */
public class PosExpression extends ExpressionNode{

	private ArithmeticOperation operation;

	public PosExpression() {
		this(ArithmeticOperation.Increment);
	}
	
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

	/**
	 * @param returningType
	 */
	public void setType(Type type) {
		this.type = type;
	}
}
