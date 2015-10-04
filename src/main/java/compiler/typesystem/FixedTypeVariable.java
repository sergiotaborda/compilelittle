/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class FixedTypeVariable implements TypeVariable {

	private TypeDefinition type;

	public FixedTypeVariable(TypeDefinition type){
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "";
	}
	
	public String toString(){
		return type.toString();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getUpperbound() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getLowerBound() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variance getVariance() {
		return Variance.Invariant;
	}



	/**
	 * @return
	 */
	public boolean isConcrete(){
		return true;
	}
}
