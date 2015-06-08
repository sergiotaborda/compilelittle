/**
 * 
 */
package compiler.sense;



/**
 * 
 */
public class MethodCallNode extends SenseAstNode {

	private String name;
	private ArgumentListNode arguments;

	/**
	 * @param string
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param argumentListNode
	 */
	public void setArgumentList(ArgumentListNode arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

}
