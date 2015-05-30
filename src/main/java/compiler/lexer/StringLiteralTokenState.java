/**
 * 
 */
package compiler.lexer;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 
 */
public class StringLiteralTokenState extends TokenState {

	/**
	 * Constructor.
	 * @param table
	 */
	public StringLiteralTokenState(TokenState other) {
		super(other.grammar);
	    this.builder = other.builder.delete(0, 1);
	}

	
	/**
	 * @param c
	 * @return
	 */
	public ParseState recieve(ScanPosition pos, char c, Consumer<Token> tokensQueue) {
		
		 if (grammar.isStringLiteralDelimiter(c)){
			 Optional<Token> token = grammar.stringLiteralMath(pos, builder.toString());
			 if (token.isPresent()){
				 tokensQueue.accept(token.get());
			 }
			 
			 return new TokenState(grammar);
		 } else {
			 builder.append(c);
		 }
		 return this;
	}
}
