/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class FixedGenericTypeParameter implements GenericTypeParameter {

	
	private TypeDefinition definition;
	private Variance variance;
	private String name;

	public FixedGenericTypeParameter (String name, TypeDefinition definition, Variance variance){
		this.definition = definition;
		this.variance = variance;
		this.name = name;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getLowerBound() {
		return definition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getUpperbound() {
		return definition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variance getVariance() {
		return variance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

}
