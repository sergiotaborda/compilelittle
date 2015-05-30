/**
 * 
 */
package compiler.parser;

import java.util.Iterator;
import java.util.List;

/**
 * 
 */
public class BagIterator implements Iterator<ParsingContext> {

	private List<ParsingContext> contexts;
	private int max;
	private int position;

	/**
	 * Constructor.
	 * @param contexts
	 * @param count
	 */
	public BagIterator(List<ParsingContext> contexts, int max) {
		this.contexts = contexts;
		this.max = max;
		this.position = -1;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return position < max - 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParsingContext next() {
		 position++;
		 return contexts.get(position);
	}
	

}
