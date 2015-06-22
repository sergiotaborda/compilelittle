/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import compiler.lexer.TokenStream;

/**
 * 
 */
public class ParsingContextBag implements Iterable<ParsingContext>{

	
	List<ParsingContext> contexts = new ArrayList<ParsingContext> ();
	int count;
	
	/**
	 * Constructor. 
	 * @param tokens
	 */
	public ParsingContextBag(TokenStream tokens ) {
	
		
		
		ParsingContext ctx = new BackedParsingContext(this, tokens);
		ctx.inputStream().moveNext();
		contexts.add(ctx);
		count = 1;
		

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<ParsingContext> iterator() {
		return new BagIterator(contexts, count);
	}

	void reset(){
		Set<ParsingContext> visited = new HashSet<>();
		for (Iterator<ParsingContext> it = contexts.iterator();it.hasNext();){
			final ParsingContext next = it.next();
			if (!next.isValid()){
				it.remove();
			}
			if (!visited.add(next)){
				it.remove();
			}
		}
		count = contexts.size();
	}

	void add(ParsingContext ctx){
		contexts.add(ctx);
	}

	/**
	 * @param backedParsingContext
	 * @param newContexts
	 */
	public void replace(BackedParsingContext remove,List<ParsingContext> add) {
		 remove.invalidate();
		 for (ParsingContext ctx : add){
			contexts.add(ctx);	
		 }
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return contexts.isEmpty();
	}

	/**
	 * @return
	 */
	public int size() {
		return contexts.size();
	}
}
