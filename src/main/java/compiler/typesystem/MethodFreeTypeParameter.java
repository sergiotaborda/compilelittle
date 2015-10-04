/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class MethodFreeTypeParameter implements TypeVariable {

	private Method method;
	protected int parameterIndex;

	public MethodFreeTypeParameter (Method method, int parameterIndex){
		this.method = method;
		this.parameterIndex = parameterIndex;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return original().getName();
	}
	
	protected TypeVariable original(){
		return method.getParameters().get(parameterIndex).getType();
	}
	
	public String toString(){
		return getUpperbound().toString();
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
	
	/**
	 * @param signature
	 */
	public TypeVariable bindGenerics(MethodSignature signature) {
		return signature.getParameters().get(parameterIndex).getType();
	}

}
