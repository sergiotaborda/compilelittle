/**
 * 
 */
package compiler.math.ast;

import java.util.Deque;

/**
 * 
 */
public class VariableNode extends MathExpressionNode{

	private String id;

	/**
	 * Constructor.
	 * @param id
	 */
	public VariableNode(String id) {
		this.id = id;
	}

	@Override
	public void operate(Deque<Value> stack) {
		
	}

}
