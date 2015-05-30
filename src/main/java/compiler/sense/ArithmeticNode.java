/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class ArithmeticNode extends ExpressionNode {

	
	private Operation operation;

	/**
	 * Constructor.
	 * @param resolveOperation
	 */
	public ArithmeticNode(Operation operation) {
		this.operation = operation;
	}

	public enum Operation {
		Multiplication,
		Addition,
		Subtraction,
		Division,
		Remainder,
		FractionDivision, RightShift, RightPositiveShift, LeftShift, Increment, Decrement,
	}
}
