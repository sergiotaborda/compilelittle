/**
 * 
 */
package compiler;

/**
 * 
 */
public interface CompilerListener {

	public void start();
	public void error(CompilerMessage error);
	public void warn(CompilerMessage error);
	public void end();
}
