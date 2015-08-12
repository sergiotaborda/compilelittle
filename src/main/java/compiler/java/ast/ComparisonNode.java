/**
 * 
 */
package compiler.java.ast;

import compiler.sense.typesystem.SenseType;


/**
 * 
 */
public class ComparisonNode extends BooleanExpressionNode {

	public enum Operation {
		LessThan ("<"),
		GreaterThan (">"),
		LessOrEqualTo ("<="),
		GreaterOrEqualTo(">="), 
		EqualTo ("=="), 
		Different ("!="),
		ReferenceEquals ("==="),
		ReferenceDifferent ("!==");
		private String symbol;

		Operation(String symbol){
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

	public ComparisonNode() {
		this(Operation.EqualTo);
	}
	
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
	
	public SenseType getType() {
		return SenseType.Boolean;
	}

	public ExpressionNode getLeft(){
		return (ExpressionNode) this.getChildren().get(0);
	}
	
	public ExpressionNode getRight(){
		return (ExpressionNode) this.getChildren().get(1);
	}
}
