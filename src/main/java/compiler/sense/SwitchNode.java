/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class SwitchNode extends SenseAstNode {

	private ExpressionNode candidate;
	private SwitchOptions switchOptions;

	/**
	 * @param expressionNode
	 */
	public void setCandidate(ExpressionNode exp) {
		this.candidate = exp;
	}

	/**
	 * @param switchOptions
	 */
	public void setOptions(SwitchOptions switchOptions) {
		this.switchOptions = switchOptions;
	}

}
