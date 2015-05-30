/**
 * 
 */
package compiler.math.ast;


/**
 * 
 */
public class Value extends ExpressionNode {

	private long value;

	/**
	 * Constructor.
	 * @param long1
	 */
	public Value(long value) {
		this.value = value;
	}

	public String toString(){
		return Long.toString(value);
	}
}
