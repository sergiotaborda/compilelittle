/**
 * 
 */
package compiler.lexer;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 
 */
public class OperatorTokenState extends TokenState {

	/**
	 * Constructor.
	 * @param table
	 */
	public OperatorTokenState(TokenState other) {
		super(other.grammar);
		this.builder = other.builder;
	}

	public ParseState recieve(ScanPosition pos,char c, Consumer<Token> tokensQueue) {

		Optional<Token> test = grammar.maybeMatch(pos,new StringBuilder().append(c).toString());

		if(test.isPresent()){
			if (test.get().isOperator()){
				// the other char is also an operator
				Optional<Token> token = grammar.maybeMatch(pos,builder.toString());

				// test together
				Optional<Token> together = grammar.maybeMatch(pos,builder.append(c).toString());

				if (together.isPresent()){

					if (together.get().isStartLineComment() ){
						return new LineCommentTokenState(grammar);
					} else if (together.get().isStartMultiLineComment() ){
						return new MultiLineCommentTokenState(grammar);
					} else if (together.get().isOperator()){
						return this;
					} else { // ID
						// two separed operators

						tokensQueue.accept(token.get());

						tokensQueue.accept(test.get());
						return new TokenState(grammar);
					}

				} else {
					throw new RuntimeException("Unrecognized " + builder.append(c));
				}
			} 

		}
		Optional<Token> token = grammar.terminalMatch(pos,builder.toString());

		if (token.isPresent()){
			tokensQueue.accept(token.get());
		}

		if (grammar.isIgnore(c)){
			return new TokenState(grammar);
		}
		return new TokenState(grammar).recieve(pos,c, tokensQueue);

	}
}
