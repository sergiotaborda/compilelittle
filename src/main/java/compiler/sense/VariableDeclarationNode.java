/**
 * 
 */
package compiler.sense;

import compiler.parser.IdentifierNode;
import compiler.typesystem.Type;

/**
 * 
 */
public class VariableDeclarationNode extends SenseAstNode implements ScopedVariableDefinitionNode{

	
	private TypeNode type;
	private String name;
	private ExpressionNode inicializer;
	private VariableInfo info;
	private ImutabilityNode imutability;
	
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
