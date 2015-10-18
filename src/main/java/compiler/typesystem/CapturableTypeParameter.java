/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class CapturableTypeParameter implements TypeVariable {



	private String name;
	private TypeDefinition type;
	private TypeSystem typeSystem;

	public CapturableTypeParameter (String name, TypeSystem system){
		this.name = name;
		this.typeSystem = system;
	}
	
	
	public void setType(TypeDefinition type){
		this.type = type;
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
		if (type == null){
			return typeSystem.MostUpperType();
		} else {
			return type;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getLowerBound() {
		if (type == null){
			return typeSystem.MostLowerType();
		} else {
			return type;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variance getVariance() {
		return Variance.Invariant;
	}


}
