/**
 * 
 */
package compiler.typesystem;

/**
 * 
 */
public class ClassBoundedTypeParameter implements TypeVariable {

	
	private TypeDefinition declaringClass;
	private Variance positionVariance;
	private int parameterIndex;

	public ClassBoundedTypeParameter (TypeDefinition declaringClass, int parameterIndex, Variance positionVariance){
		this.positionVariance = positionVariance;
		this.declaringClass = declaringClass;
		this.parameterIndex= parameterIndex;
	}
	
	private TypeVariable original(){
		return null; //declaringClass.getGenericParameters().get(parameterIndex);
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
