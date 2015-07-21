/**
 * 
 */
package compiler.sense;

import compiler.parser.IdentifierNode;
import compiler.sense.typesystem.SenseType;
import compiler.syntax.AstNode;
import compiler.typesystem.Type;

/**
 * 
 */
public class FieldDeclarationNode extends AnnotadedSenseAstNode implements ScopedVariableDefinitionNode{

	private TypeNode typeNode;
	private String name;
	private ExpressionNode inicializer;
	private VariableInfo info;
	private ImutabilityNode imutability;

	
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
	public void setName(IdentifierNode variableNameNode) {
		this.name = variableNameNode.getId();
		this.add(variableNameNode);
	}

	public void setInicializer(ExpressionNode inicializer) {
		this.inicializer = inicializer;
		this.add(inicializer);
	}

	public Type getType() {
		return typeNode.getType();
	}

	public String getName() {
		return name;
	}

	/**
	 * Atributes {@link String}.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtains {@link ExpressionNode}.
	 * @return the inicializer
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
	}

	/**
	 * @param info
	 */
	public void setInfo(VariableInfo info) {
		this.info = info;
	}
	
	public VariableInfo getInfo(){
		return info;
	}

	/**
	 * @param astNode
	 */
	public void setImutability(ImutabilityNode imutability) {
		this.imutability = imutability;
	}
	
	public Imutability getImutability() {
		return this.imutability == null ? Imutability.Mutable : imutability.getImutability();
	}
	
}
