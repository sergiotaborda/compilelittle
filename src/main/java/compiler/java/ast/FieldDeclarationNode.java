/**
 * 
 */
package compiler.java.ast;

import compiler.typesystem.Type;
import compiler.typesystem.VariableInfo;

/**
 * 
 */
public class FieldDeclarationNode extends AnnotadedAstNode implements ScopedVariableDefinitionNode{

	private TypeNode typeNode;
	private String name;
	private ExpressionNode initializer;
	private VariableInfo info;
	private ImutabilityNode imutability;

	
	/**
	 * @param typeNode
	 */
	public void setTypeNode(TypeNode typeNode) {
		this.typeNode = typeNode;
		this.add(typeNode);
	}

	public TypeNode getTypeNode(){
		return typeNode;
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
		return initializer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInitializer(ExpressionNode node) {
		this.initializer = node;
		this.add(initializer);
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
	
	public ImutabilityNode getImutability() {
		return this.imutability;
	}
	
	public Imutability getImutabilityValue() {
		return this.imutability == null ? Imutability.Mutable : imutability.getImutability();
	}
	
}
