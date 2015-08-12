/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.ast.BooleanOperatorNode.BooleanOperation;
import compiler.typesystem.Type;

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
