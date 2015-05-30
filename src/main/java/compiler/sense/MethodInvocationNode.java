/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class MethodInvocationNode extends ExpressionNode {

	private MethodCallNode call;
	private AstNode access;

	/**
	 * @param methodCallNode
	 */
	public void setCall(MethodCallNode call) {
		this.call = call;
	}

	/**
	 * @param astNode
	 */
	public void setAccess(AstNode node) {
		this.access = node;
	}

}
