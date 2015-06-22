/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import compiler.Grammar;

/**
 * 
 */
public abstract class AbstractLookupTable implements LookupTable{

	protected List<LookupTableRow> rows = new ArrayList<>();
	protected Set<Production> columns = new LinkedHashSet<>();
	private Grammar grammar;

	public AbstractLookupTable (Grammar grammar,int rowsCount){
		this.grammar = grammar;
		rows = new ArrayList<>(rowsCount);
		
		columns.add(EOFTerminal.instance());
		
	}
	
	protected Grammar getGrammar(){
		return grammar;
	}

	public Collection<Production> columns (){
		return columns;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int rowCount() {
		return rows.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LookupTableAction getAction(StackItem item, StackItem peek) {
		
		if (item instanceof StateStackItem){
			StateStackItem s = (StateStackItem)item;
			
			return rows.get(s.getStateId()).matchWith(peek);
			
		} else {
			return new NoAction();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<LookupTableRow> iterator() {
		return this.rows.iterator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LookupTableRow getRow(int stateId) {
		return this.rows.stream().filter(r -> r.getId() == stateId).findFirst().get();
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		
		builder.append("\t");
		for(Production p  : columns){
			builder.append(p).append("\t");
		}
		builder.append("\n");
		for(LookupTableRow r : rows){
			builder.append(r.id).append("\t");
			for(Production p  : columns){
				LookupTableAction action = r.getActionFor(p);
				
				builder.append(action.toString());
			
				
				builder.append("\t");
			}
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
	public boolean equals(Object other){
		
		return other instanceof AbstractLookupTable && equals((AbstractLookupTable)other);
	}

	public boolean equals(AbstractLookupTable other){
		if( this.columns.size() == other.columns.size() 
				&& this.columns.containsAll(other.columns())
				&& this.rows.size() == other.rows.size()){
			
			for(int i=0; i < this.rowCount();i++){
				this.rows.get(i).equals(other.rows.get(i));
			}
			
			return true;
		}
		return false;
			
			
	}
	
	public int hashCode(){
		return this.rowCount();
	}
}
