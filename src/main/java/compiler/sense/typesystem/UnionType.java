/**
 * 
 */
package compiler.sense.typesystem;

/**
 * 
 */
public class UnionType extends Type {

	/**
	 * Constructor.
	 * @param name
	 */
	public UnionType(Type a, Type b) {
		super(a.toString() + "|" + b.toString());
	
	}
	
}
