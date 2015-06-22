/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 
 */
public class ProductionSequence extends AbstractProduction implements Iterable<Production>{

	
	List<Production> sequence = new ArrayList<>(3);
	

	public ProductionSequence(Production first){
		sequence.add(first);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductionSequence add(Production other) {
		sequence.add(other);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSequence() {
		return true;
	}

	public Production get(int index){
		return sequence.get(index);
	}

	public String toString(){
		return this.getLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return this.label == null ? sequence.toString(): this.label;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Production> iterator() {
		return sequence.iterator();
	}


	/**
	 * @param i
	 */
	public Production remove(int index) {
		return sequence.remove(index);
	}

	/**
	 * @return
	 */
	public int size() {
		return sequence.size();
	}

	public Production getFirst() {
		return sequence.get(0);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof ProductionSequence) && equalsNonTerminal((ProductionSequence)obj); 
	}


	private boolean equalsNonTerminal(ProductionSequence other) {
		return Arrays.equals(this.sequence.toArray(), other.sequence.toArray());
	}

	public int hashCode (){
		return this.sequence.size();
	}

}
