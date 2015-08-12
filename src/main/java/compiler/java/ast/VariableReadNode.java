/**
 * 
 */
package compiler.java.ast;

import compiler.typesystem.Type;
import compiler.typesystem.VariableInfo;


/**
 * 
 */
public class VariableReadNode extends ExpressionNode {

	private String name;
	private VariableInfo variableInfo;
	
	public VariableReadNode() {
		this.name = "";
	}
	
	/**
	 * Constructor.
	 * @param id
	 */
	public VariableReadNode(String name) {
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
