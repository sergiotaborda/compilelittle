/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public interface TypeResolver {

	/**
	 * @param filter
	 * @return
	 */
	TypeDefinition resolveTypeByName(TypeSearchParameters filter);

	/**
	 * @param type
	 */
	void registerType(TypeDefinition type);


}
