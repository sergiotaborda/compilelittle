/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;

/**
 * 
 */
public class GenericType extends Type {

	/**
	 * Constructor.
	 * @param name
	 * @param superType
	 */
	public GenericType(java.lang.String name, Type superType) {
		super(name, superType);
	}
	
	protected boolean isGeneric(){
		return true;
	}


}
