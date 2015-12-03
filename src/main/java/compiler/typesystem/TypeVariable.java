/**
 * 
 */
package compiler.typesystem;

/**
 * 
 */
public interface TypeVariable {

	public String getName();

	public TypeDefinition getUpperbound();

	public TypeDefinition getLowerBound();

	/**
	 * Obtains {@link Variance}.
	 * @return the variance
	 */
	public Variance getVariance();

	/**
	 * @return
	 */
	public default boolean isConcrete(){
		return false;
	}

}