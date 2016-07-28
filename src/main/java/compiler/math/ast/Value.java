/**
 * 
 */
package compiler.math.ast;

import java.util.Deque;

/**
 * 
 */
public class Value extends MathExpressionNode {

	protected long value;

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

	@Override
	public void operate(Deque<Value> stack) {
		stack.push(this);
	}

	public long getValue() {
		return value;
	}
}
