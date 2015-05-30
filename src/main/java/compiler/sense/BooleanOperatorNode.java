/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class BooleanOperatorNode extends ExpressionNode {

	private Operation operation;

	/**
	 * Constructor.
	 * @param resolveBooleanOperation
	 */
	public BooleanOperatorNode(Operation operation) {
		this.operation = operation;
	}

	enum Operation {
		BitAnd,
		BitOr,
		BitXor,
		ShortAnd,
		ShortOr, Negate, InstanceofType, BitNegate
	}
}
