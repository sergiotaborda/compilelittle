/**
 * 
 */
package compiler;

import compiler.lexer.ScanPositionHolder;

/**
 * 
 */
public class CompilerMessage {

	
	private final String message;
	private final ScanPositionHolder holder;

	public CompilerMessage(compiler.CompilationError error) {
		this.message = error.getMessage();
		this.holder = error.getScanPositionHolder();
	}
	public CompilerMessage (String message){
		this(message, null);
	}
	
	public CompilerMessage(String message, ScanPositionHolder holder) {
		this.message = message;
		this.holder = holder;
	}
	
	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	
	public ScanPositionHolder getPosition() {
		return holder;
	}

}
