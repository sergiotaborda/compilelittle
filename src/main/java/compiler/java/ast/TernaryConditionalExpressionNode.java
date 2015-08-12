/**
 * 
 */
package compiler.java.ast;

import compiler.sense.typesystem.SenseType;
import compiler.typesystem.Type;


/**
 * 
 */
public class TernaryConditionalExpressionNode extends ExpressionNode implements ConditionalStatement{
	
	ExpressionNode thenExpression;
	ExpressionNode elseExpression;
	private ExpressionNode conditional;
	
	/**
	 * @param astNode
	 */
	public void Condition(ExpressionNode conditional) {
		this.conditional = conditional;
		this.add(conditional);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExpressionNode getCondition() {
		return conditional;
	}
}
