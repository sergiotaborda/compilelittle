/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public interface Member extends TypeMember {

	public TypeDefinition getDeclaringType();

	//public int getModifiers();

	public boolean isSynthetic();
}
