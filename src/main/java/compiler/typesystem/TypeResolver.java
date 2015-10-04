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


}
