/**
 * 
 */
package compiler.lexer;

import java.util.function.Consumer;

/**
 * 
 */
public class LineCommentTokenState extends TokenState {

	/**
	 * Constructor.
	 * @param table
	 */
	public LineCommentTokenState(TokenState currentState) {
		super(currentState.getScanner());
	}

	
	@Override
	public ParseState receive(ScanPosition pos,char c, Consumer<Token> tokensQueue) {
		
		if (c == '\n'){
			return  this.getScanner().newInitialState().receive(pos, c, tokensQueue);
		}
		return this;
	}
}
