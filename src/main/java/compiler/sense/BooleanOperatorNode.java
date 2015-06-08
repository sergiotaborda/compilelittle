/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class BooleanOperatorNode extends BooleanExpressionNode {

	enum Operation {
		BitAnd,
		BitOr,
		BitXor,
		ShortAnd,
		ShortOr, Negate, InstanceofType, BitNegate
	}
	
	private Operation operation;

	/**
	 * Constructor.
	 * @param resolveBooleanOperation
	 */
	public BooleanOperatorNode(Operation operation) {
		this.operation = operation;
	}
	


}
