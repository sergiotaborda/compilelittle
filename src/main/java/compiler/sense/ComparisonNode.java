/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class ComparisonNode extends BooleanExpressionNode {

	enum Operation {
		LessThan,
		GreaterThan,
		LessOrEqualTo,
		GreaterOrEqualTo, EqualTo, Different, ReferenceEquals
	}
	
	private Operation operation;

	/**
	 * Constructor.
	 * @param resolveComparisonOperation
	 */
	public ComparisonNode(Operation operation) {
		this.operation = operation;
	}

	public Operation getOperation() {
		return operation;
	}
	
	public Type getType() {
		return Type.Boolean;
	}


}
