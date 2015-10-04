
/**
 * 
 */
package compiler.sense.ast;


import compiler.typesystem.TypeDefinition;


/**
 * 
 */
public class ExpressionNode extends SenseAstNode implements TypedNode {

	private TypeDefinition type;

	public TypeDefinition getTypeDefinition() {
		return type;
	}

	public void setTypeDefinition(TypeDefinition type) {
		this.type = type;
	}
}
