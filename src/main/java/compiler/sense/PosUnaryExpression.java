/**
 * 
 */
package compiler.sense;

import compiler.sense.BooleanOperatorNode.Operation;
import compiler.sense.typesystem.Type;

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
	
	public Type getType(){
		return ((ExpressionNode)this.getChildren().get(0)).getType();
	}

}
