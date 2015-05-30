package compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import compiler.RealizedPromisseSet;

public class ProductionItem {

	Production root;
	List<Production> productions = new ArrayList<>();
	private int nextReadablePosition = 0;
	boolean isAugmented = false;
	List<SemanticAction> semanticActions = new ArrayList<SemanticAction>(0);

	private RealizedPromisseSet<MatchableProduction> lookAhead;

	ProductionItem(MatchableProduction p){
		lookAhead = new RealizedPromisseSet<MatchableProduction>();
		lookAhead.add(p);
	}

	ProductionItem(RealizedPromisseSet<MatchableProduction> set){
		lookAhead = set;
	}

	public boolean isShift(){
		final Production nextReadable = this.getNextReadable();
		return nextReadable != null && (nextReadable.isTerminal() || nextReadable.isAutoNonTerminal());
	}
	
	/**
	 * Constructor.
	 * @param productionItem
	 * @param other
	 */
	public ProductionItem(ProductionItem other, RealizedPromisseSet<MatchableProduction> lookAhead) {
		this.lookAhead = lookAhead;
		this.root = other.root;
		this.productions = other.productions;
		this.nextReadablePosition = other.nextReadablePosition;
		this.isAugmented = other.isAugmented;
		this.semanticActions = other.semanticActions;
	}

	public RealizedPromisseSet<MatchableProduction> getLookAhead(){
		return lookAhead;
	}

	public void executeSemanticActions( Symbol left , List<Symbol> right){
		for(SemanticAction sa : semanticActions){
			sa.execute(left, right);
		}
	}

	public boolean isAugmented (){
		return this.isAugmented;
	}

	public String toString(){
		StringBuilder builder = new StringBuilder(root.getLabel()).append("\u2192");

		if (productions.size() == 1)
		{
			if (nextReadablePosition == 0)
			{
				builder.append('\u2022');
			}

			builder.append(productions.get(0).toString());



		} else {
			for(int i =0; i < productions.size(); i++){

				if (i == nextReadablePosition){
					builder.append('\u2022');
				}

				builder.append(productions.get(i).toString()).append(" ");
			}
		}

		if (nextReadablePosition == productions.size())
		{
			builder.append('\u2022');
		}


		builder.append(" ");
		builder.append(this.lookAhead.toString());

		return builder.toString();
	}

	public Production getNextReadable(){
		return nextReadablePosition >= productions.size() ? null : productions.get(nextReadablePosition);
	}

	/**
	 * @return
	 */
	public Iterator<Production> getAllAfterNextReadable() {
		return productions.listIterator(nextReadablePosition + 1);
	}
	
	public Optional<Production> geAfterNextReadable(){
		if (nextReadablePosition == productions.size() - 1){
			return Optional.empty();
		}
		return Optional.of(productions.get(nextReadablePosition + 1));
	}

	public Production getPreviousRead(){
		return productions.get(nextReadablePosition -1);
	}

	public boolean isFinal(){
		return nextReadablePosition == productions.size();
	}

	public ProductionItem advance(){
		if (this.isFinal()){
			return this;
		}

		ProductionItem item = new ProductionItem(this.lookAhead);
		item.root = this.root;
		item.isAugmented = this.isAugmented;
		item.productions = this.productions;
		item.nextReadablePosition = this.nextReadablePosition + 1;
		item.semanticActions = this.semanticActions;
		return item;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = nextReadablePosition;
		result = prime * result + lookAhead.size();
		result = prime * result + productions.size();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return equals((ProductionItem)obj);
	}

	private boolean equals(ProductionItem other){
		return this.nextReadablePosition == other.nextReadablePosition 
				&& this.productions.size() == other.productions.size()
				&& this.lookAhead.size() == other.lookAhead.size()
				&& this.lookAhead.equals(other.lookAhead)
				&& Arrays.equals(this.productions.toArray(), ((ProductionItem)other).productions.toArray());
				
	}

	public boolean equalsIgnoreLookAhead(ProductionItem other) {
		return this.nextReadablePosition == other.nextReadablePosition 
				&& this.productions.size() == other.productions.size()
				&& Arrays.equals(this.productions.toArray(), ((ProductionItem)other).productions.toArray());
	}

	public void merge(ProductionItem other) {
		this.lookAhead = this.lookAhead.union(other.lookAhead);
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return this.productions.isEmpty() && this.nextReadablePosition == 0;
	}

	/**
	 * @param lookAhead2
	 * @return
	 */
	public ProductionItem relook(RealizedPromisseSet<MatchableProduction> other) {
		if (this.lookAhead.equals(other)){
			return this;
		} else {
			return new ProductionItem(this, other);
		}
	}


}
