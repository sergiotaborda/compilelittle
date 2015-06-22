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

	public int hashCode(){
		return type.hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof TypeParameter && this.equals(((TypeParameter)other));
	}
	
	public boolean equals(TypeParameter other){
		return this.variance == other.variance && this.type.equals(other.type);
	}
	
}
