/**
 * 
 */
package compiler;

import java.util.Optional;

import compiler.lexer.ScanPosition;
import compiler.lexer.Scanner;
import compiler.lexer.Token;
import compiler.parser.Production;
import compiler.parser.ProductionItem;

/**
 * 
 */
public abstract class Grammar {

	/**
	 * @param targetId
	 * @return
	 */
	public abstract ProductionItem getFinalProductionItem(int targetId);
	public abstract int  getFinalProductionItemTargetId(ProductionItem item);
	/**
	 * @param c
	 * @return
	 */
	public abstract boolean isStopCharacter(char c);

	/**
	 * @param builder
	 */
	public abstract Optional<Token> maybeMatch(ScanPosition pos,String text);

	/**
	 * match made after a terminal char is encountered.
	 * the match should occur, of there is a possible error.
	 * @param builder
	 */
	public abstract Optional<Token> terminalMatch(ScanPosition pos,String text);
	
	/**
	 * @param c
	 * @return
	 */
	public abstract boolean isIgnore(char c);

	/**
	 * @return
	 */
	public abstract boolean isStringLiteralDelimiter(char c);

	/**
	 * @param string
	 * @return
	 */
	public abstract Optional<Token> stringLiteralMath(ScanPosition pos,String string);
	
	/**
	 * 
	 */
	public abstract Production getStartProduction();

	public Scanner scanner() {
		return new Scanner(this);
	}

	/**
	 * @param c
	 * @return
	 */
	public boolean isNumberStarter(char c) {
		return c == '1' || c == '2' ||c == '3' ||c == '4' ||c == '5' ||c == '6' ||c == '7' || c == '8' || c == '9' ||  c== '#';
	}
	
	public boolean isDigit(char c) {
		return isNumberStarter(c) || c == '0' || c == '_' || c == '.' || c == 'e' || c=='E';
	}
	

	public boolean isAlphabetic(char c) {
		return Character.isAlphabetic(c);
	}

}
