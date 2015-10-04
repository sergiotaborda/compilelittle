/**
 * 
 */
package compiler;

/**
 * 
 */
public class CompilationError extends RuntimeException {

	private static final long serialVersionUID = 5239871682414732773L;
	
	public CompilationError(String msg) {
		super(msg);
	}
}
