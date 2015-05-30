/**
 * 
 */
package compiler.bnf;

import compiler.syntax.AstNode;

/**
 * 
 */
public class Rule extends AstNode {

	private String name;

	/**
	 * @param string
	 */
	public void setName(String name) {
		this.name  = name;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public AstNode getExpression() {
		return this.getChildren().get(0);
	}

	
	public String toString(){
		return name;
	}
	
	public boolean equals(Object other){
		return other instanceof Rule && ((Rule)other).name.equals(this.name);
	}
	
	public int hashCode(){
		return name.hashCode();
	}
}
