/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public interface TypeResolver {

	/**
	 * @param name
	 * @return
	 */
	Type resolveTypeByName(String name);

}
