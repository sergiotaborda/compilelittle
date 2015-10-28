/**
 * 
 */
package compiler.typesystem;

/**
 * 
 */
public interface TypeSystem {

	/**
	 * @return
	 */
	TypeDefinition MostUpperType();

	/**
	 * @return
	 */
	TypeDefinition MostLowerType();

}
