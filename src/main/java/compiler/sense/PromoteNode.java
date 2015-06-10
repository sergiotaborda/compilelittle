/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class PromoteNode extends ExpressionNode {

	private ExpressionNode other;

	/**
	 * Constructor.
	 * @param type 
	 * @param inicializer
	 */
	public PromoteNode(ExpressionNode other, Type type) {
		this.other = other;
		this.add(other);
		this.type = type;
	}

}
