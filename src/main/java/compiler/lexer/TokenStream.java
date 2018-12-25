/**
 * 
 */
package compiler.lexer;

/**
 * 
 */
public interface TokenStream {

	public boolean hasNext();
	public boolean hasPrevious();
	public Token next();
	public Token peekPrevious();
	public Token peekNext();
	
	/**
	 * @return
	 */
	public TokenStream duplicate();
}
