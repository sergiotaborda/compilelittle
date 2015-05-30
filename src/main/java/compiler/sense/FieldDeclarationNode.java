/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class FieldDeclarationNode extends AstNode{

	private TypeNode typeNode;
	private VariableNameNode variableNameNode;

	/**
	 * @param typeNode
	 */
	public void setType(TypeNode typeNode) {
		this.typeNode = typeNode;
		this.add(typeNode);
	}

	/**
	 * @param variableNameNode
	 */
	public void setVariableName(VariableNameNode variableNameNode) {
		this.variableNameNode = variableNameNode;
		this.add(variableNameNode);
	}

}
