/**
 * 
 */
package compiler.sense.typesystem;


/**
 * 
 */
public class UnionType extends SenseTypeDefinition {

	/**
	 * Constructor.
	 * @param name
	 */
	public UnionType(SenseTypeDefinition a, SenseTypeDefinition b) {
		super(a.toString() + "|" + b.toString(), Kind.Class, null);
	
	}
	
}
