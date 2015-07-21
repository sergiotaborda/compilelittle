/**
 * 
 */
package compiler.lexer;

import java.util.function.Consumer;

/**
 * 
 */
public interface ParseState  {

	/**
	 * @param pos 
	 * @param c
	 * @return
	 */
	public ParseState receive(ScanPosition pos, char c, Consumer<Token> tokensQueue);

	/**
	 * @param pos 
	 * 
	 */
	public void clear(ScanPosition pos, Consumer<Token> tokensQueue);


}