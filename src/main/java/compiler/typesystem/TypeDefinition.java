/**
 * 
 */
package compiler.typesystem;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 
 */
public interface TypeDefinition {

	
	public String getName();

	public String getSimpleName();
	
	public TypeKind getKind();
	
	public List<TypeMember> getMembers();
	
	public TypeDefinition getSuperDefinition();

	public List<GenericTypeParameter> getGenericParameters();

	public Optional<Constructor> getConstructorByParameters(MethodParameter ... parameters);
		
	/**
	 * @param name
	 * @return
	 */
	public Optional<Field> getFieldByName(String name);

	/**
	 * @param fieldName
	 * @return
	 */
	public Optional<Property> getPropertyByName(String fieldName);

	/**
	 * @param string
	 * @return
	 */
	public Collection<Method> getMethodsByName(String string);

	/**
	 * @param signature
	 * @return
	 */
	public Optional<Method> getMethodBySignature(MethodSignature signature);
	
	public Optional<Method> getMethodByPromotableSignature(MethodSignature signature);


}
