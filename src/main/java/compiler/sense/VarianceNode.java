/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.TypeParameter.Variance;
import compiler.syntax.AstNode;

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
