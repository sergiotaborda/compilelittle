/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.TypeDefinition;


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
	
	public TypeDefinition getTypeDefinition() {
		return SenseTypeSystem.Boolean();
	}

	public ExpressionNode getLeft(){
		return (ExpressionNode) this.getChildren().get(0);
	}
	
	public ExpressionNode getRight(){
		return (ExpressionNode) this.getChildren().get(1);
	}
}
