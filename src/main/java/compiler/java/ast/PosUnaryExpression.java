/**
 * 
 */
package compiler.java.ast;

import compiler.sense.ast.BooleanOperatorNode.BooleanOperation;
import compiler.typesystem.TypeDefinition;

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
	
	public TypeDefinition getTypeDefinition(){
		return ((ExpressionNode)this.getChildren().get(0)).getTypeDefinition();
	}
	
	public BooleanOperation getOperation(){
		return operation;
	}

}
