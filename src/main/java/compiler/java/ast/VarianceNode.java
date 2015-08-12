/**
 * 
 */
package compiler.java.ast;

import compiler.syntax.AstNode;
import compiler.typesystem.TypeParameter.Variance;

/**
 * 
 */
public class VarianceNode extends AstNode {

	private Variance variance;

	/**
	 * Constructor.
	 * @param variant
	 */
	public VarianceNode(Variance variance) {
		this.variance = variance;
	}

	public Variance getVariance() {
		return variance;
	}


}
