/**
 * 
 */
package compiler.typesystem;

/**
 * 
 */
public class MethodFreeBoxedTypeParameter extends MethodFreeTypeParameter {


	private int boxedParameterIndex;

	public MethodFreeBoxedTypeParameter (Method method, int parameterIndex, int boxedParameterIndex){
		super(method,parameterIndex);
		this.boxedParameterIndex = boxedParameterIndex;
	}
	
	public TypeVariable original(){
		return null; //super.original().getUpperbound().getGenericParameters().get(boxedParameterIndex);
	}

	public TypeVariable replicate(Method newDeclaringMethod) {
		return new MethodFreeBoxedTypeParameter(newDeclaringMethod, this.parameterIndex,this.boxedParameterIndex);
	}
	

	/**
	 * @param signature
	 */
	public TypeVariable bindGenerics(MethodSignature signature) {
		return null; //signature.getParameters().get(parameterIndex).getType().getUpperbound().getGenericParameters().get(boxedParameterIndex);
	}
}
