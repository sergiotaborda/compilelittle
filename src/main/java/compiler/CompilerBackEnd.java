/**
 * 
 */
package compiler;


/**
 * Acts as a final CompilePhase.
 */
public interface CompilerBackEnd extends CompilationUnitConsumer{


	public default void beforeAll() {};

	public default void afterAll() {};

}
