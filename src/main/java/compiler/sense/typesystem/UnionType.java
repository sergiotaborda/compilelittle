/**
 * 
 */
package compiler.sense.typesystem;

import compiler.typesystem.Type;

/**
 * 
 */
public class UnionType extends SenseType {

	/**
	 * Constructor.
	 * @param name
	 */
	public UnionType(Type a, Type b) {
		super(a.toString() + "|" + b.toString());
	
	}
	
}
