/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.SenseType;


/**
 * 
 */
public class NumericValue extends LiteralExpressionNode {

	private Number number;

	/**
	 * @param n
	 */
	public void setValue(Number n, SenseType type) {
		this.number = n;
		this.type = type;
	}
	
	public String toString(){
		return number.toString();
	}

}
