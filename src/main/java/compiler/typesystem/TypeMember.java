/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public interface TypeMember {

	/**
	 * @return
	 */
	boolean isField();

	/**
	 * @return
	 */
	String getName();

	/**
	 * @return
	 */
	boolean isProperty();

	/**
	 * @return
	 */
	boolean isMethod();
	
	/**
	 * @return
	 */
	boolean isConstructor();
	
	public TypeDefinition getDeclaringType();

	/**
	 * @param concrete
	 * @return
	 */
	TypeMember changeDeclaringType(TypeDefinition concrete);

}