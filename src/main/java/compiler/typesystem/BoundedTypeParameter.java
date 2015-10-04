/**
 * 
 */
package compiler.typesystem;

import compiler.sense.typesystem.SenseTypeSystem;


/**
 * 
 */
public class BoundedTypeParameter implements TypeVariable {

	
	private TypeDefinition upperBound;
	private TypeDefinition lowerBound;
	private Variance variance;
	private String name;
	
	public BoundedTypeParameter(Variance variance, String name) {
		this.upperBound = SenseTypeSystem.Any();
		this.lowerBound = SenseTypeSystem.Nothing();
		this.variance = variance;
		this.name = name;
	}
	
	public String toString(){
		return getUpperbound().toString();
	}
	
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
	public BoundedTypeParameter(Variance variance, String name, TypeDefinition upperBound, TypeDefinition lowerBound) {
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.variance = variance;
		this.name = name;
	}

	/**
	 * Constructor.
	 * @param t
	 */
	public BoundedTypeParameter(TypeVariable t) {
		this.upperBound = t.getUpperbound();
		this.lowerBound = t.getLowerBound();
		this.variance = t.getVariance();
		this.name = t.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName(){
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getUpperbound() {
		return upperBound;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getLowerBound() {
		return lowerBound;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variance getVariance() {
		return variance;
	}


	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof BoundedTypeParameter && this.equals(((BoundedTypeParameter)other));
	}
	
	public boolean equals(BoundedTypeParameter other){
		return this.variance == other.variance 
				&& this.name.equals(other.name);
	}

	/**
	 * @param types
	 */
	public void setConcreteType(TypeDefinition type) {
		this.upperBound = type;
		this.lowerBound = type;
	}




}
