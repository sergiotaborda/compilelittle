/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;
import compiler.typesystem.Type;

/**
 * 
 */
public class AssignmentNode extends ExpressionNode {

	enum Operation {
		SimpleAssign ("="), 
		MultiplyAndAssign("*="), 
		DivideAndAssign ("/="), 
		RemainderAndAssign ("%="), 
		AddAndAssign("+="), 
		SubtractAndAssign ("-="), 
		LeftShiftAndAssign ("<<="),
		RightShiftAndAssign(">>="), 
		PositiveRightShiftAndAssign(">>>="), 
		BitAndAndAssign ("&="),
		BitXorAndAssign ("^="), 
		BitOrAndAssign ("|=");

		private String symbol;

		private Operation (String symbol){
			this.symbol = symbol;
		}
		
		/**
		 * @return
		 */
		public String symbol() {
			return symbol;
		}
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

	/**
	 * @return
	 */
	public Operation getOperation() {
		return operation;
	}

}
