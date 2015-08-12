/**
 * 
 */
package compiler.sense;

import compiler.Grammar;
import compiler.lexer.ParseState;
import compiler.lexer.Scanner;
import compiler.lexer.StringLiteralTokenState;
import compiler.lexer.TokenState;

/**
 * 
 */
public class SenseScanner extends Scanner {

	/**
	 * Constructor.
	 * @param grammar
	 */
	public SenseScanner(Grammar grammar) {
		super(grammar);
	}

	/**
	 * @param tokenState
	 * @return
	 */
	public ParseState getStringLiteralTokenState(TokenState tokenState) {
		return new SenseStringLiteralTokenState(tokenState);
	}
}
