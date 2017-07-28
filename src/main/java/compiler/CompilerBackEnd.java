/**
 * 
 */
package compiler;


/**
 * Acts as a final CompilePhase.
 */
public interface CompilerBackEnd extends CompilationUnitConsumer{


    default void beforeAll() {};

    default void afterAll() {};

}
