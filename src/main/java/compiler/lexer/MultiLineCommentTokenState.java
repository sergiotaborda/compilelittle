/**
 * 
 */
package compiler.lexer;

import java.util.Optional;
import java.util.function.Consumer;

import compiler.Grammar;

/**
 * 
 */
public class MultiLineCommentTokenState extends TokenState {

	private int commentsOpen = 1;
	
	/**
	 * Constructor.
	 * @param table
	 */
	public MultiLineCommentTokenState(TokenState currentState) {
		super(currentState.getScanner());
	}

	@Override
	public ParseState receive(ScanPosition pos,char c, Consumer<Token> tokensQueue) {
		
		Grammar grammar = this.getScanner().getGrammar();
		if (grammar.isStopCharacter(c)){
			if (!grammar.isIgnore(c)){
				builder.append(c);
			}
			// interrupt
			Optional<Token> test = grammar.maybeMatch( pos,builder.toString());
			if (test.isPresent() && test.get().isEndMultiLineComment()){
				commentsOpen--;
				if (commentsOpen==0){
					return this.getScanner().newInitialState();
				} else {
					builder = builder.delete(0, builder.length() - 1);
					return this;
				}
			} else if (test.isPresent() && test.get().isStartMultiLineComment()){
				commentsOpen++;
				builder = builder.delete(0, builder.length() - 1);
				return this;
			} else if (builder.length() > 0){
				builder = builder.delete(0, builder.length() - 1);
			}
		} else {
			builder.append(c);
			Optional<Token> test = grammar.maybeMatch( pos,builder.toString());
			
			if (test.isPresent() && test.get().isEndMultiLineComment()){
				return this.getScanner().newInitialState();
			} else if (builder.length() > 0){
				builder = builder.delete(0, builder.length() - 1);
			}
		}
		return this;
	}
}
