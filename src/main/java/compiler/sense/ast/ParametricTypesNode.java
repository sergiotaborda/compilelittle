/**
 * 
 */
package compiler.sense.ast;

import compiler.typesystem.TypeDefinition;
import compiler.typesystem.Variance;


/**
 * 
 */
public class ParametricTypesNode extends SenseAstNode implements TypedNode {

	
	private TypeNode typeNode;
	private Variance variance;

	
	public ParametricTypesNode (){}
	
	/**
	 * Constructor.
	 * @param typeNode2
	 * @param variance2
	 */
	public ParametricTypesNode(TypeNode typeNode, Variance variance) {
		this.typeNode = typeNode;
		this.variance = variance;
	}

	public TypeNode getTypeNode() {
		return typeNode;
	}

	public void setTypeNode(TypeNode typeNode) {
		this.typeNode = typeNode;
		this.add(typeNode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getTypeDefinition() {
		return typeNode.getTypeDefinition();
	}

	public Variance getVariance() {
		return variance;
	}

	public void setVariance(Variance variance) {
		this.variance = variance;
	}
}
