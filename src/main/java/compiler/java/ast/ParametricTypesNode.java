/**
 * 
 */
package compiler.java.ast;

import compiler.typesystem.Type;
import compiler.typesystem.TypeParameter.Variance;


/**
 * 
 */
public class ParametricTypesNode extends JavaAstNode implements TypedNode {

	
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
