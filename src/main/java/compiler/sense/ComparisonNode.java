/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class ComparisonNode extends ExpressionNode {

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

	enum Operation {
		LessThan,
		GreaterThan,
		LessOrEqualTo,
		GreaterOrEqualTo, EqualTo, Different, ReferenceEquals
	}
}
