/**
 * 
 */
package compiler.parser;

import compiler.lexer.Token;

/**
 * 
 */
public interface MatchableProduction extends Production{

	
	public boolean match(Token token);
}
