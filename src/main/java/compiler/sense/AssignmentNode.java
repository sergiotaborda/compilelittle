/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class AssignmentNode extends AstNode {

	enum Operation {
		SimpleAssign, 
		MultiplyAndAssign, 
		DivideAndAssign, 
		RemainderAndAssign, 
		AddAndAssign, 
		SubtractAndAssign, 
		LeftShiftAndAssign,
		RightShiftAndAssign, 
		PositiveRightShiftAndAssign, 
		BitAndAndAssign,
		BitXorAndAssign, 
		BitOrAndAssign
	}

	private Operation operation;
	
	/**
	 * Constructor.
	 * @param resolveAssignmentOperation
	 */
	public AssignmentNode(Operation operation) {
		this.operation = operation;
	}
	

	/**
	 * @param astNode
	 */
	public void setLeft(AstNode left) {
		this.add(left);
	}

	/**
	 * @param astNode
	 */
	public void setRight(AstNode right) {
		this.add(right);
	}

}
