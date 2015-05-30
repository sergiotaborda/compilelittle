/**
 * 
 */
package compiler.bnf;

import compiler.syntax.AstNode;

/**
 * 
 */
public class Literal extends AstNode {

	private String name;

	/**
	 * @param id
	 */
	public void setName(String name) {
		this.name= name;
	}

	public String getName (){
		return name;
	}
}
