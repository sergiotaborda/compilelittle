/**
 * 
 */
package compiler.sense;

import compiler.typesystem.Type;

/**
 * 
 */
public class VariableWriteNode extends ExpressionNode {

	private String name;
	private VariableInfo variableInfo;
	
	/**
	 * Constructor.
	 * @param id
	 */
	public VariableWriteNode(String name) {
		this.name = name;
	}
	

	/**
	 * @param object
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param variableInfo
	 */
	public void setVariableInfo(VariableInfo variableInfo) {
		this.variableInfo = variableInfo;
	}

	public Type getType(){
		return variableInfo.getType();
	}

}
