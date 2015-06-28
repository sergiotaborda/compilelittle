/**
 * 
 */
package compiler.bnf;

import compiler.syntax.AstNode;


/**
 * 
 */
public class RulesAlternative extends BnfAstNode {

	public void add(BnfAstNode node) {
		if (node instanceof RulesAlternative){
			RulesAlternative alt = (RulesAlternative)node;
			
			for (AstNode n : alt.getChildren()){
				super.add(n);
			}
			
		} else {
			super.add(node);
		}
	}
}
