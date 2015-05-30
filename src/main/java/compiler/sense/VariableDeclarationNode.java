/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class VariableDeclarationNode extends AstNode {

	
	private TypeNode type;
	private VariableNameNode name;
	
	public TypeNode getType() {
		return type;
	}

	public void setType(TypeNode type) {
		this.type = type;
	}

	public VariableNameNode getName() {
		return name;
	}

	public void setName(VariableNameNode name) {
		this.name = name;
	}
}
