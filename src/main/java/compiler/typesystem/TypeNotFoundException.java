/**
 * 
 */
package compiler.typesystem;

/**
 * 
 */
public class TypeNotFoundException extends compiler.SyntaxError{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5916705841908507990L;

	/**
	 * Constructor.
	 * @param msg
	 */
	public TypeNotFoundException(String msg) {
		super(msg);
	}

}
