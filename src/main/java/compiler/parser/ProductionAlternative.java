/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 */
public class ProductionAlternative extends AbstractProduction implements Iterable<Production> {

	List<Production> alternatives = new ArrayList<>(3);
	
	
	public ProductionAlternative(Production first){
		alternatives.add(first);
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductionAlternative or(Production other) {
		if (other.isAlternative()){
			for(Production p : other.toAlternative()){
				alternatives.add(p);
			}
		} else {
			alternatives.add(other);
		}
	
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAlternative() {
		return true;
	}
	
	public String toString(){
		return this.getLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return this.label == null ? toString(alternatives): this.label;
	}
	
	/**
	 * @param alternatives2
	 * @return
	 */
	private static String toString(List<Production> a) {
		StringBuilder builder = new StringBuilder("[");
		
		for(int i =0; i < a.size(); i++){
			builder.append(a.get(i).toString()).append(" | ");
			
		}
		builder.delete(builder.length()-3, builder.length()-1);
		builder.append("]");
		return builder.toString();
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Production> iterator() {
		return alternatives.iterator();
	}

	public Production get(int i) {
		return alternatives.get(i);
	}


}
