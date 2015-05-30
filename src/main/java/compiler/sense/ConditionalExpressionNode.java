/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class ConditionalExpressionNode extends ExpressionNode {

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
		this.add(exp);
	}

	/**
	 * @param expressionNode
	 */
	public void FalsePath(ExpressionNode exp) {
		this.add(exp);
	}

}
