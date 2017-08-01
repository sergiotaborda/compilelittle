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

	public boolean isStartLineComment();

	public boolean isStartMultiLineComment();

	public boolean isId();

	public boolean isEndMultiLineComment();

	public boolean isEndOfFile();

	public boolean isEndOfLine();

	public boolean isStringLiteralStart();


	public boolean  isStringLiteral();

	public default boolean isLiteral(){
	    return this.isStringLiteral() || this.isNumberLiteral();
	}

	public default boolean isNumberLiteral() {
	    return this.isWholeNumber() || this.isDecimalNumber();
	}

	public boolean isWholeNumber();

	public boolean isDecimalNumber();

	public boolean isOperator();

	public boolean match(String text);

	public Optional<String> getText();

	public boolean isKeyword();


}