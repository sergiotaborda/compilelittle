/**
 * 
 */
package compiler.lexer;

/**
 * 
 */
public interface TokenStream {

	public boolean hasNext();
	public Token next();
	/**
	 * @return
	 */
	public TokenStream duplicate();
}
