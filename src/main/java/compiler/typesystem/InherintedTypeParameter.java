/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class InherintedTypeParameter implements TypeVariable {

	
	private TypeDefinition declaringClass;
	private int superTypeParameterIndex;
	private Variance positionVariance;

	public InherintedTypeParameter(TypeDefinition declaringClass, int superTypeParameterIndex,Variance positionVariance){
		this.declaringClass = declaringClass;
		this.superTypeParameterIndex = superTypeParameterIndex;
		this.positionVariance = positionVariance;
	}
	
	private GenericTypeParameter original(){
		return declaringClass.getSuperDefinition().getGenericParameters().get(superTypeParameterIndex);
	}
	
	public String toString(){
		return getUpperbound().toString();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return original().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getUpperbound() {
		if (this.positionVariance == Variance.ContraVariant){
			return original().getLowerBound();
		} else {
			return original().getUpperbound();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getLowerBound() {
		if (this.positionVariance == Variance.ContraVariant){
			return original().getUpperbound();
		} else {
			return original().getLowerBound();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variance getVariance() {
		return positionVariance;
	}



}
