/**
 * 
 */
package compiler.sense;

import compiler.sense.BooleanOperatorNode.BooleanOperation;
import compiler.sense.typesystem.Type;

/**
 * 
 */
public class PosUnaryExpression extends ExpressionNode {

	private BooleanOperation operation;

	/**
	 * Constructor.
	 * @param resolveBooleanOperation
	 */
	public PosUnaryExpression(BooleanOperation operation) {
		this.operation = operation;
	}
	
	public Type getType(){
		return ((ExpressionNode)this.getChildren().get(0)).getType();
	}
	
	public BooleanOperation getOperation(){
		return operation;
	}

}
