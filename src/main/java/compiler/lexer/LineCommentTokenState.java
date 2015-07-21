/**
 * 
 */
package compiler.lexer;

import java.util.function.Consumer;

import compiler.Grammar;

/**
 * 
 */
public class LineCommentTokenState extends TokenState {

	/**
	 * Constructor.
	 * @param table
	 */
	public LineCommentTokenState(Grammar table) {
		super(table);
	}

	
	@Override
	public ParseState receive(ScanPosition pos,char c, Consumer<Token> tokensQueue) {
		
		if (c == '\n'){
			return new TokenState(grammar).receive(pos, c, tokensQueue);
		}
		return this;
	}
}
