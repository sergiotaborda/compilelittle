/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class ImutabilityNode extends AstNode {

	private Imutability imutability = Imutability.Mutable;
	
	public ImutabilityNode (){}

	/**
	 * Constructor.
	 * @param mutable
	 */
	public ImutabilityNode(Imutability imutability) {
		this.imutability = imutability;
	}

	public Imutability getImutability() {
		return imutability;
	}

}
