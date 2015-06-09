/**
 * 
 */
package compiler.parser;

import compiler.lexer.TokenStream;

/**
 * 
 */
class TokenStreamStackInputStreamAdapter implements StackInputStream {

	private TokenStream stream;
	TokenStackItem current;

	/**
	 * Constructor.
	 * @param tokens
	 */
	public TokenStreamStackInputStreamAdapter(TokenStream tokens) {
		stream = tokens;
	}


	/**
	 * Constructor.
	 * @param tokenStreamStackInputStreamAdapter
	 */
	public TokenStreamStackInputStreamAdapter(TokenStreamStackInputStreamAdapter other) {
		stream = other.stream.duplicate();
		current = other.current;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveNext() {
		current = new TokenStackItem(stream.next());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return stream.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TokenStackItem currentItem() {
		return current;
	}

	/**
	 * @return
	 */
	public TokenStreamStackInputStreamAdapter duplicate() {
		return new TokenStreamStackInputStreamAdapter(this);
	}

	public String toString(){
		return current + "#" + stream;
	}


	@Override
	public boolean equals(Object obj) {
		return (obj instanceof TokenStreamStackInputStreamAdapter) && equalsTokenStreamStackInputStreamAdapter((TokenStreamStackInputStreamAdapter)obj); 
	}


	private boolean equalsTokenStreamStackInputStreamAdapter(TokenStreamStackInputStreamAdapter other) {
		return this.current.equals(other.current) && this.stream.equals(other.stream);
	}
}
