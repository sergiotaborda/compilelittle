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
		LessThan ("<"),
		GreaterThan (">"),
		LessOrEqualTo ("<="),
		GreaterOrEqualTo(">="), 
		EqualTo ("=="), 
		Different ("!="),
		ReferenceEquals ("===");
		
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

	public ExpressionNode getLeft(){
		return (ExpressionNode) this.getChildren().get(0);
	}
	
	public ExpressionNode getRight(){
		return (ExpressionNode) this.getChildren().get(1);
	}
}
