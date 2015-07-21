/**
 * 
 */
package compiler.sense;

import compiler.SymbolBasedToken;
import compiler.TokenSymbol;
import compiler.lexer.Token;
import compiler.lexer.TokenStream;

/**
 * 
 */
class SenseAwareTokenStream implements TokenStream {

	private TokenStream original;

	/**
	 * Constructor.
	 * @param tokens
	 */
	public SenseAwareTokenStream(TokenStream original) {
		this.original = original;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return original.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token next() {
		Token t = original.next();
		if (t.isKeyword() && (t.getText().get().equals("out") || t.getText().get().equals("in") )){
			Token previous = original.peekPrevious();
			if (previous.isOperator() && previous.getText().get().equals(".")){
				// if keywords are used after a . they are not consider keywords. 
				// This is to maintain compatability with System.in and System.out
				return new SymbolBasedToken(t.getPosition(), t.getText().get(),TokenSymbol.ID );
			}
		} 
		return t;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TokenStream duplicate() {
		return new SenseAwareTokenStream(original.duplicate());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPrevious() {
		return original.hasPrevious();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token peekPrevious() {
		return original.peekPrevious();
	}

}
