/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;
import compiler.syntax.AstNode;

/**
 * 
 */
public class AssignmentNode extends ExpressionNode {

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
	private AstNode left;
	private ExpressionNode right;
	
	/**
	 * Constructor.
	 * @param resolveAssignmentOperation
	 */
	public AssignmentNode(Operation operation) {
		this.operation = operation;
	}
	
	public Type getType() {
		return right.getType();
	}
	
	/**
	 * @param astNode
	 */
	public void setLeft(AstNode left) {
		this.left = left;
		this.add(left);
	}

	/**
	 * @param astNode
	 */
	public void setRight(ExpressionNode right) {
		this.right = right;
		this.add(right);
	}

	/**
	 * @return
	 */
	public TypedNode getLeft() {
		return (TypedNode)this.left;
	}

	/**
	 * @return
	 */
	public ExpressionNode getRight() {
		return this.right;
	}

}
