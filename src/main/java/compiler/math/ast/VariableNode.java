/**
 * 
 */
package compiler.math.ast;


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

}
