/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;

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
