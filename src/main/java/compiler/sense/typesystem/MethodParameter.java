/**
 * 
 */
package compiler.sense.typesystem;

import compiler.typesystem.Type;

/**
 * 
 */
public interface MethodParameter {

	/**
	 * Obtains {@link string}.
	 * @return the name
	 */
	public String getName();

	/**
	 * Obtains {@link SenseType}.
	 * @return the type
	 */
	public Type getType();

}