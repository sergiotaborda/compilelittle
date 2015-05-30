/**
 * 
 */
package compiler.lexer;

import java.util.Optional;

/**
 * 
 */
public class EOLToken implements Token {

	private ScanPosition position;

	public EOLToken(){}
	
	public EOLToken(ScanPosition position){
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
		return false;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEndOfLine() {
		return true;
	}
	
	public String toString(){
		return "EOL";
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
		return other instanceof EOLToken;
	}
	
	public int hashCode (){
		return -2;
	}
}
