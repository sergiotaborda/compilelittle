/**
 * 
 */
package compiler.sense.typesystem;

/**
 * 
 */
public class TypeParameter {

	
	public enum Variance {
		Invariant,
		Variant,
		Covariant
	}
	
	private Type type;
	private Variance variance;

	/**
	 * Constructor.
	 * @param g
	 */
	public TypeParameter(Type type, Variance variance) {
		this.type = type;
		this.variance = variance;
	}

	/**
	 * Obtains {@link Type}.
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Obtains {@link Variance}.
	 * @return the variance
	 */
	public Variance getVariance() {
		return variance;
	}

	/**
	 * @param type2
	 */
	public boolean isAssignableTo(Type type) {
		return this.type.isAssignableTo(type);
	}

	
}
