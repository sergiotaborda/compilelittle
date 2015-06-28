/**
 * 
 */
package compiler.bnf;

import compiler.syntax.AstNode;


/**
 * 
 */
public class RulesSequence extends BnfAstNode {

	/**
	 * @return
	 */
	public RulesSequence duplicate() {
		RulesSequence copy = new RulesSequence();
		
		for (AstNode n : this.getChildren()){
			copy.add(n);
		}
		
		return copy;
	}
	
	public String toString(){
		return this.getChildren().toString();
	}
	
	


}
