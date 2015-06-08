/**
 * 
 */
package compiler.sense;

import compiler.SymbolBasedToken;
import compiler.TokenSymbol;
import compiler.lexer.ScanPosition;

/**
 * 
 */
public class SenseToken extends SymbolBasedToken {

	/**
	 * Constructor.
	 * @param position
	 * @param text
	 * @param symbol
	 */
	public SenseToken(ScanPosition position, String text, TokenSymbol symbol) {
		super(position, text, symbol);
	}

}
