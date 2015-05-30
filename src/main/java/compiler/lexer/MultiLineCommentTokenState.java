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

	/**
	 * Constructor.
	 * @param table
	 */
	public MultiLineCommentTokenState(Grammar table) {
		super(table);
	}

	@Override
	public ParseState recieve(ScanPosition pos,char c, Consumer<Token> tokensQueue) {
		
		if (grammar.isStopCharacter(c)){
			if (!grammar.isIgnore(c)){
				builder.append(c);
			}
			// interrupt
			Optional<Token> test = grammar.maybeMatch( pos,builder.toString());
			if (test.isPresent() && test.get().isEndMultiLineComment()){
				return new TokenState(grammar);
			} else if (builder.length() > 0){
				builder = builder.delete(0, builder.length() - 1);
			}
		} else {
			builder.append(c);
			Optional<Token> test = grammar.maybeMatch( pos,builder.toString());
			
			if (test.isPresent() && test.get().isEndMultiLineComment()){
				return new TokenState(grammar);
			} else if (builder.length() > 0){
				builder = builder.delete(0, builder.length() - 1);
			}
		}
		return this;
	}
}
