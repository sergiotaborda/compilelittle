/**
 * 
 */
package compiler;

/**
 * 
 */
public interface Compiler {

	public CompilationResultSet parse(CompilationUnitSet unitSet);
	public void setCompilerListener(CompilerListener listener);
}
