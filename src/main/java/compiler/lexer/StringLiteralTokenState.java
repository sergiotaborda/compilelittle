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
public class StringLiteralTokenState extends TokenState {

	private TokenState state;


	/**
	 * Constructor.
	 * @param table
	 */
	public StringLiteralTokenState(TokenState currentState) {
		super(currentState.getScanner());
	    this.builder = currentState.builder.delete(0, 1);
	}

	
	/**
	 * @param c
	 * @return
	 */
	public ParseState receive(ScanPosition pos, char c, Consumer<Token> tokensQueue) {
		
		 final Grammar grammar = this.getScanner().getGrammar();
		if (grammar.isStringLiteralDelimiter(c)){
			 Optional<Token> token = grammar.stringLiteralMath(pos, builder.toString());
			 if (token.isPresent()){
				 tokensQueue.accept(token.get());
			 }
			 
			 return this.getScanner().newInitialState();
		 } else {
			 builder.append(c);
		 }
		 return this;
	}
}
