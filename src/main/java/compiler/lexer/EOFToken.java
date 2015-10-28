/**
 * 
 */
package compiler.lexer;

import java.util.Optional;


/**
 * 
 */
public class EOFToken implements Token {

	private ScanPosition position;

	public EOFToken(){}
	
	public EOFToken(ScanPosition position){
		this.position = new ScanPosition(position);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScanPosition getPosition() {
		return position;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStartLineComment() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStartMultiLineComment() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isId() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEndMultiLineComment() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEndOfFile() {
		return true;
	}
	
	public String toString(){
		return "EOF";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStringLiteralStart() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNumberLiteral() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOperator() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(String text) {
		return false;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEndOfLine() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getText() {
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStringLiteral() {
		return false;
	}
	
	public boolean equals(Object other){
		return other instanceof EOFToken;
	}
	
	public int hashCode (){
		return -1;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWholeNumber() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDecimalNumber() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isKeyword() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVersionLiteral() {
		return false;
	}
}
