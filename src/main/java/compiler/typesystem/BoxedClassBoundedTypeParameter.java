/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class BoxedClassBoundedTypeParameter implements TypeVariable {

	
	private String name;
	private TypeVariable[] boxedTypes;
	private TypeDefinition boxedType;

	public BoxedClassBoundedTypeParameter (String name, TypeDefinition boxedType , TypeVariable... boxedTypes){
		this.name = name;
		this.boxedType = boxedType;
		this.boxedTypes = boxedTypes;
			
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getUpperbound() {
		return null; //boxedType.of(boxedTypes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getLowerBound() {
		return null; // boxedType.of(boxedTypes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variance getVariance() {
		return Variance.Invariant;
	}
}
