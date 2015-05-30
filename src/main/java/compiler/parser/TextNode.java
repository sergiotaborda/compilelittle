/**
 * 
 */
package compiler.parser;

import compiler.syntax.AstNode;

/**
 * 
 */
public class TextNode extends AstNode {

	private String text;

	/**
	 * Constructor.
	 * @param string
	 */
	public TextNode(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}


}
