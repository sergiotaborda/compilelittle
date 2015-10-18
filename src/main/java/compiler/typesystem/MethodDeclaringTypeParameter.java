/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class MethodDeclaringTypeParameter implements TypeVariable {

	
	private Method method;
	private int parameterIndex;

	public MethodDeclaringTypeParameter (Method method, int index){
		this.method = method;
		this.parameterIndex = index;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		 return original().getName();
	}


	private GenericTypeParameter original() {
		return method.getDeclaringType().getGenericParameters().get(parameterIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getUpperbound() {
		return original().getUpperbound();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getLowerBound() {
		return original().getLowerBound();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Variance getVariance() {
		return Variance.Invariant;
	}

}
