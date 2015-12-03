/**
 * 
 */
package compiler;


/**
 * Acts as a final CompilePhase.
 */
public interface CompilerBackEnd {

	/**
	 * @param transform
	 */
	void use(CompiledUnit unit);

}
