/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.TypeParameter.Variance;
import compiler.typesystem.Type;


/**
 * 
 */
public class ParametricTypesNode extends SenseAstNode implements TypedNode {

	
	private TypeNode typeNode;
	private Variance variance;

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
	public Type getType() {
		return typeNode.getType();
	}

	public Variance getVariance() {
		return variance;
	}

	public void setVariance(Variance variance) {
		this.variance = variance;
	}
}
