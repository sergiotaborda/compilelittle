/**
 * 
 */
package compiler.sense.ast;


/**
 * 
 */
public class ImutabilityNode extends SenseAstNode {

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
