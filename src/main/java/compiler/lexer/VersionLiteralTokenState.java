/**
 * 
 */
package compiler.lexer;

import java.util.function.Consumer;

import compiler.Grammar;

/**
 * 
 */
public class VersionLiteralTokenState extends TokenState{

	/**
	 * Constructor.
	 * @param scanner
	 */
	public VersionLiteralTokenState(Scanner scanner) {
		super(scanner);
	}

	/**
	 * Constructor.
	 * @param tokenState
	 */
	public VersionLiteralTokenState(TokenState other) {
		super(other.getScanner());
		this.builder = other.builder;
	}

	public ParseState receive(ScanPosition pos,char c, Consumer<Token> tokensQueue) {
		Grammar grammar = this.getScanner().getGrammar();
		
		if (grammar.isStopCharacter(c) ){
			tokensQueue.accept(grammar.terminalMatch(pos,builder.toString()).get());
			return this.getScanner().newInitialState().receive(pos, c, tokensQueue);
		 } else {
			 builder.append(c);
		 }
		 return this;
		
	}
}
