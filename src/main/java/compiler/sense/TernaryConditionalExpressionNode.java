/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class TernaryConditionalExpressionNode extends ExpressionNode {
	
	ExpressionNode thenExpression;
	ExpressionNode elseExpression;
	
	/**
	 * @param astNode
	 */
	public void Condition(ExpressionNode exp) {
		this.add(exp);
	}

	/**
	 * @param expressionNode
	 */
	public void TruePath(ExpressionNode exp) {
		thenExpression= exp;
		this.add(exp);
	}

	/**
	 * @param expressionNode
	 */
	public void FalsePath(ExpressionNode exp) {
		elseExpression = exp;
		this.add(exp);
	}

	public Type getType() {
		return thenExpression.getType().or(elseExpression.getType());
	}
}
