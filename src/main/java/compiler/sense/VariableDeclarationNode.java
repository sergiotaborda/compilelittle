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
	 * @param expressionNode
	 */
	public void setInicializer(ExpressionNode expressionNode) {
		this.inicializer = expressionNode;
		this.add(expressionNode);
	}

	/**
	 * 
	 */
	public ExpressionNode getInicializer() {
		return inicializer;
	}
}
