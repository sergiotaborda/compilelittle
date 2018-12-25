/**
 * 
 */
package compiler.lexer;

import java.util.List;
import java.util.Optional;

/**
 * 
 */
public class ListTokenStream implements TokenStream {


	private List<Token> tokens;
	private int position;

	public ListTokenStream(List<Token> tokens){
		this(tokens, -1);
	}

	ListTokenStream(List<Token> tokens, int startPosition){
		this.tokens = tokens;
		this.position = startPosition;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return position < tokens.size() - 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token next() {
		position++;
		return tokens.get(position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TokenStream duplicate() {
		return new ListTokenStream(tokens, position);
	}

	public String toString(){
		return this.tokens.subList(position + 1, this.tokens.size()).toString();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof ListTokenStream) && equalsListTokenStream((ListTokenStream)obj); 
	}


	private boolean equalsListTokenStream(ListTokenStream other) {
		return this.position == other.position && this.tokens == other.tokens;
	}
	
    public int hashCode() {
    	return this.position;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasPrevious() {
		return position  > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token peekPrevious() {
		return tokens.get(position - 1);
	}
	
	@Override
	public Token peekNext() {
		return tokens.get(position + 1);
	}
}
