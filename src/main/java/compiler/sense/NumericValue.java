/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class NumericValue extends LiteralExpressionNode {

	private Number number;

	/**
	 * @param n
	 */
	public void setValue(Number n) {
		this.number = n;
	}

	
}
