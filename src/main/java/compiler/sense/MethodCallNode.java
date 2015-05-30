/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;


/**
 * 
 */
public class MethodCallNode extends AstNode {

	private String name;
	private ArgumentListNode arguments;

	/**
	 * @param string
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param argumentListNode
	 */
	public void setArgumentList(ArgumentListNode arguments) {
		this.arguments = arguments;
	}

}
