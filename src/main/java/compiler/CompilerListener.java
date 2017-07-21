/**
 * 
 */
package compiler;

/**
 * 
 */
public interface CompilerListener {

	public void start();
	public void error(CompilerMessage message);
	public void warn(CompilerMessage message);
	public void trace(CompilerMessage message);
	public void end();
}
