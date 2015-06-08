/**
 * 
 */
package compiler.sense;

/**
 * 
 */
public interface ScopedVariableDefinitionNode extends TypedNode {

	/**
	 * @return
	 */
	String getName();

	/**
	 * @return
	 */
	TypedNode getInicializer();

}
