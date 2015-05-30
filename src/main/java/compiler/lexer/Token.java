/**
 * 
 */
package compiler.lexer;

import java.util.Optional;

/**
 * 
 */
public interface Token {

	public ScanPosition getPosition();
	
	/**
	 * @return
	 */
	 boolean isStartLineComment();

	/**
	 * @return
	 */
	 boolean isStartMultiLineComment();

	/**
	 * @return
	 */
	 boolean isId();

	/**
	 * @return
	 */
	 boolean isEndMultiLineComment();

	 boolean isEndOfFile();

	 boolean isEndOfLine();
	/**
	 * @return
	 */
	 boolean isStringLiteralStart();

		/**
		 * @return
		 */
	 boolean  isStringLiteral();
		
	/**
	 * @return
	 */
	 boolean isNumberLiteral();

	/**
	 * @return
	 */
	 boolean isOperator();

	/**
	 * @param text
	 * @return
	 */
	boolean match(String text);
	
	Optional<String> getText();


}