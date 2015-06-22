/**
 * 
 */
package compiler.sense;

import compiler.parser.IdentifierNode;
import compiler.sense.typesystem.Type;
import compiler.syntax.AstNode;

/**
 * 
 */
public class VariableDeclarationNode extends SenseAstNode implements ScopedVariableDefinitionNode{

	
	private TypeNode type;
	private String name;
	private ExpressionNode inicializer;
	
	public Type getType() {
		return type.getType();
	}

	public TypeNode getTypeNode() {
		return type;
	}

	
	public void setType(TypeNode type) {
		this.type = type;
		this.add(type);
	}

	public String getName() {
		return name;
	}

	public void setName(IdentifierNode name) {
		this.name = name.getId();
	}


	/**
	 * 
	 */
	public ExpressionNode getInitializer() {
		return inicializer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInitializer(ExpressionNode node) {
		this.inicializer = node;
		this.add(node);
	}
}
