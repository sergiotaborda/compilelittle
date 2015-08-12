/**
 * 
 */
package compiler.sense.ast;

import compiler.typesystem.Type;


/**
 * 
 */
public class PromoteNode extends ExpressionNode {

	private ExpressionNode other;
	private Type from;

	/**
	 * Constructor.
	 * @param type 
	 * @param inicializer
	 */
	public PromoteNode(ExpressionNode other, Type from, Type to) {
		this.other = other;
		this.add(other);
		this.setType(to);
		this.from = from;
	}

}
