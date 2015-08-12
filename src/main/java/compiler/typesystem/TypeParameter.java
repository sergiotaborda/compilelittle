/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class TypeParameter {

	
	public enum Variance {
		Invariant, // The type is fixed. Variant and Covariant positions have the same type of the upperbound
		ContraVariant, // in. Only values in the arguments list have the same type as upperbound.
		Covariant // out. Only values returning have the same type as upperbound
	}
	
	private Type upperBound;
	private Type lowerBound;
	private Variance variance;
	private String name;
	
	/**
	 * Example 1 : interface Sequence<out T> 
	 *     name: T 
	 *     variance : Covariance
	 *     upperBound : Any
	 *     lowerBound : Nothing
	 * Example 2 : void send<T extends Imutable | Serializable> (T message) 
	 *     name: T 
	 *     variance : Contravariant
	 *     upperBound : Imutable | Serializable
	 *     lowerBound : Nothing
	 *     
	 *     
	 * Constructor.
	 * @param variance
	 * @param name
	 * @param upperBound
	 * @param lowerBound
	 */
	public TypeParameter(Variance variance, String name, Type upperBound, Type lowerBound) {
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.variance = variance;
		this.name = name;
	}

	/**
	 * Constructor.
	 * @param t
	 */
	public TypeParameter(TypeParameter other) {
		this.upperBound = other.upperBound;
		this.lowerBound = other.lowerBound;
		this.variance = other.variance;
		this.name = other.name;
	}

	public String getName(){
		return name;
	}
	
	/**
	 * Obtains {@link SenseType}.
	 * @return the type
	 */
	public Type getUpperbound() {
		return upperBound;
	}
	
	public Type getLowerBound() {
		return lowerBound;
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
		return lowerBound.isAssignableTo(type) && type.isAssignableTo(upperBound);
	}

	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof TypeParameter && this.equals(((TypeParameter)other));
	}
	
	public boolean equals(TypeParameter other){
		return this.variance == other.variance 
				&& this.name.equals(other.name);
	}

	/**
	 * @param types
	 */
	public void setConcreteType(Type type) {
		this.upperBound = type;
		this.lowerBound = type;
	}
	
}
