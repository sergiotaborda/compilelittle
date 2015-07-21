/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public interface TypesRepository {

	/**
	 * @param name
	 * @return
	 */
	Type resolveTypeByName(String name);

}
