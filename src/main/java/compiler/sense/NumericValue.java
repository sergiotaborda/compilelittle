/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class NumericValue extends LiteralExpressionNode {

	private Number number;

	/**
	 * @param n
	 */
	public void setValue(Number n, Type type) {
		this.number = n;
		this.type = type;
	}

}
