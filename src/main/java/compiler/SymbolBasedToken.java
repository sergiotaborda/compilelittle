/**
 * 
 */
package compiler;

import java.util.Optional;

import compiler.lexer.ScanPosition;
import compiler.lexer.Token;

/**
 * 
 */
public class SymbolBasedToken implements Token{

	
	private String text;
	private TokenSymbol symbol;
	private ScanPosition position;

	public SymbolBasedToken(ScanPosition position, String text, TokenSymbol symbol){
		this.text = text;
		this.symbol = symbol;
		this.position = new ScanPosition(position);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isKeyword() {
		return symbol == TokenSymbol.KeyWord;
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
	public boolean isStringLiteral() {
		return symbol == TokenSymbol.LiteralString;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStartLineComment() {
		return symbol == TokenSymbol.StartInlineComment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStartMultiLineComment() {
		return symbol == TokenSymbol.StartMultilineComment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEndMultiLineComment() {
		return symbol == TokenSymbol.EndMultilineComment;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isId() {
		return symbol == TokenSymbol.ID;
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
	public boolean isStringLiteralStart() {
		return symbol == TokenSymbol.LiteralStringStart;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNumberLiteral() {
		return symbol == TokenSymbol.LiteralWholeNumber || symbol == TokenSymbol.LiteralDecimalNumber;
	}


	public boolean isWholeNumber() {
		return symbol == TokenSymbol.LiteralWholeNumber;
	}
	
	public boolean isDecimalNumber() {
		return symbol == TokenSymbol.LiteralDecimalNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOperator() {
		return symbol == TokenSymbol.Operator;
	}

	public String toString(){
		return  text + "\t" +  symbol.name() ;
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
	public boolean match(String text) {
		return this.text.equals(text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getText() {
		return Optional.of(text);
	}
	
	public boolean equals(Object other){
		return other instanceof SymbolBasedToken && ((SymbolBasedToken)other).text.equals(this.text) && ((SymbolBasedToken)other).symbol.equals(this.symbol);
	}
	
	public int hashCode (){
		return this.text.hashCode();
	}






}
