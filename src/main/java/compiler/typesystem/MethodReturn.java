/**
 * 
 */
package compiler.typesystem;

/**
 * 
 */
public class MethodReturn implements MethodMember {

	private Method method;
	private TypeVariable type;
	
	public MethodReturn(TypeVariable type){
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Method getDeclaringMethod() {
		return method;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDeclaringMethod(Method method) {
		this.method = method;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PositionalVariance getPositionVariance() {
		return PositionalVariance.Out;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeVariable getType() {
		return type;
	}

}
