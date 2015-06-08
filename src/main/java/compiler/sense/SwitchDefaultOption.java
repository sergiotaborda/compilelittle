/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class SwitchDefaultOption extends SenseAstNode {

	private AstNode actions;

	/**
	 * @param astNode
	 */
	public void setActions(AstNode astNode) {
		this.actions = astNode;
	}

}
