/**
 * 
 */
package compiler.lexer;

import java.util.Optional;


/**
 * 
 */
public class EmptyToken implements Token {

	
	private ScanPosition position;

	public EmptyToken(){}
	
	public EmptyToken(ScanPosition position){
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
	
	public String toString(){
		return "£";
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
		return other instanceof EmptyToken;
	}
	
	public int hashCode (){
		return 1;
	}


}
