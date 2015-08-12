/**
 * 
 */
package compiler.sense.ast;

import compiler.typesystem.Type;



/**
 * 
 */
public class FieldAccessNode extends NeedTypeCalculationNode {

	private String name;

	public FieldAccessNode() {
		this.name = null;
	}
	/**
	 * Constructor.
	 * @param string
	 */
	public FieldAccessNode(String name) {
		this.name = name;
	}

	/**
	 * @param string
	 */
	public void setName(String name) {
		this.name= name;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setType(Type type){
		super.setType(type);
	}
}
