/**
 * 
 */
package compiler.typesystem;

import compiler.sense.typesystem.SenseTypeSystem;

/**
 * 
 */
public class CapturableTypeParameter implements TypeVariable {



	private String name;
	private TypeDefinition type;

	public CapturableTypeParameter (String name){
		this.name = name;
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
			return SenseTypeSystem.Any();
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
			return SenseTypeSystem.Nothing();
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
