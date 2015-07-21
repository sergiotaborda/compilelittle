/**
 * 
 */
package compiler.lexer;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 
 */
public class NumberLiteralTokenState extends TokenState {

	/**
	 * Constructor.
	 * @param table
	 */
	public NumberLiteralTokenState(TokenState other) {
		super(other.grammar);
		this.builder = other.builder;
	}

	/**
	 * @param c
	 * @return
	 */
	public ParseState receive(ScanPosition pos,char c, Consumer<Token> tokensQueue) {
		if ( c == '.' && builder.toString().contains(".")){
			
			// test together
			Optional<Token> together = grammar.maybeMatch(pos,"..");

			if (together.isPresent()){
				
				builder.deleteCharAt(builder.length() -1);
				// accept number
				tokensQueue.accept(grammar.terminalMatch(pos,builder.toString()).get());
				
				builder = new StringBuilder("..");
				
				return new OperatorTokenState(this);
			} else {
				tokensQueue.accept(grammar.terminalMatch(pos,builder.toString()).get());
				return new TokenState(grammar).receive(pos, c, tokensQueue);
			}
		} else if ( grammar.isDigit(c)){
			builder.append(c);
		} else if (grammar.isAlphabetic(c)){
			if (builder.charAt(builder.length() - 1) == '.'){
				builder.deleteCharAt(builder.length() - 1);
				tokensQueue.accept(grammar.terminalMatch(pos,builder.toString()).get());
				tokensQueue.accept(grammar.terminalMatch(pos,".").get());
			} else {
				tokensQueue.accept(grammar.terminalMatch(pos,builder.toString()).get());
			}
			
			return new TokenState(grammar).receive(pos, c, tokensQueue);
		} else if (grammar.isStopCharacter(c) ){
			tokensQueue.accept(grammar.terminalMatch(pos,builder.toString()).get());
			return new TokenState(grammar).receive(pos, c, tokensQueue);
		 } else {
			 builder.append(c);
		 }
		 return this;
	}
}
