/**
 * 
 */
package compiler.typesystem;

/**
 * 
 */
public interface MethodMember {

	public TypeVariable getType();
	public Method getDeclaringMethod();
	public void  setDeclaringMethod(Method method);
	public PositionalVariance getPositionVariance();
}
