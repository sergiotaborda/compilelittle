/**
 * 
 */
package compiler.sense.ast;

import compiler.syntax.AstNode;

/**
 * 
 */
public class MethodInvocationNode extends NeedTypeCalculationNode {

	private MethodCallNode call;
	private AstNode access;
	
	public MethodInvocationNode (){}
	
	public MethodInvocationNode (AstNode access ,String name, AstNode ... arguments){
		setCall(new MethodCallNode(name, new ArgumentListNode(arguments)));
		setAccess(access);
	}

	/**
	 * @param methodCallNode
	 */
	public void setCall(MethodCallNode call) {
		this.call = call;
		this.add(call);
	}

	/**
	 * @param astNode
	 */
	public void setAccess(AstNode access) {
		this.access = access;
		this.add(access);
	}

	/**
	 * Obtains {@link MethodCallNode}.
	 * @return the call
	 */
	public MethodCallNode getCall() {
		return call;
	}

	/**
	 * Obtains {@link AstNode}.
	 * @return the access
	 */
	public AstNode getAccess() {
		return access;
	}
	
	public void replace(AstNode node, AstNode newnode){
		super.replace(node, newnode);
		
		if (this.access == node){
			this.access = newnode;
		}
	}
	

}
