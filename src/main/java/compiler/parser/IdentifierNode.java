/**
 * 
 */
package compiler.parser;

import compiler.syntax.AstNode;

/**
 * 
 */
public class IdentifierNode extends AstNode {

	private String id;

	/**
	 * Constructor.
	 * @param string
	 */
	public IdentifierNode(String id) {
		this.id = id;
	}

	public String getId(){
		return id;
	}
}
