/**
 * 
 */
package compiler.parser;

import compiler.syntax.AstNode;

/**
 * 
 */
public class IdentifierNode extends AstNode implements NameIdentifierNode{

	private String id;

	/**
	 * Constructor.
	 * @param string
	 */
	public IdentifierNode(String id) {
		this.id = id;
	}

	public String getName(){
		return id;
	}
	
	public String toString() {
		return id;
	}
}
