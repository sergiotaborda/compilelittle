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
public class OperatorTokenState extends TokenState {

	/**
	 * Constructor.
	 * @param table
	 */
	public OperatorTokenState(TokenState other) {
		super(other.getScanner());
		this.builder = other.builder;
	}

	public ParseState receive(ScanPosition pos,char c, Consumer<Token> tokensQueue) {

		Grammar grammar = this.getScanner().getGrammar();
		
		Optional<Token> test = grammar.maybeMatch(pos,new StringBuilder().append(c).toString());

		if(test.isPresent()){
			if (test.get().isOperator()){
				// the other char is also an operator
				Optional<Token> token = grammar.maybeMatch(pos,builder.toString());

				// test together
				Optional<Token> together = grammar.maybeMatch(pos,builder.append(c).toString());

				if (together.isPresent()){

					if (together.get().isStartLineComment() ){
						return this.getScanner().getLineCommentTokenState(this);
					} else if (together.get().isStartMultiLineComment() ){
						return this.getScanner().getMultiLineCommentTokenState(this);
					} else if (together.get().isOperator()){
						return this;
					} else { // ID
						// two separed operators

						tokensQueue.accept(token.get());

						tokensQueue.accept(test.get());
						return this.getScanner().newInitialState();
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
			return this.getScanner().newInitialState();
		}
		return this.getScanner().newInitialState().receive(pos,c, tokensQueue);

	}
}
