package compiler.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class LookupTable implements Iterable<LookupTableRow>{

	public List<LookupTableRow> rows = new ArrayList<>();
	public Set<Production> columns = new LinkedHashSet<>();
	public Set<ItemState> states = new LinkedHashSet<>();
	
	public LookupTable(List<ItemState> states) {
		rows = new ArrayList<>(states.size());
		
		for (ItemState s : states){
			rows.add(new LookupTableRow(this,s.getId()));
		}
		columns.add(EOFTerminal.instance());
	}
	
	public List<ItemState> getStates() {
		ArrayList<ItemState> list = new ArrayList<>(states);
		list.sort( (a , b) -> a.getId() - b.getId());
		return list;
	}

	public void addReduce(ItemState current, Production p, ProductionItem target ) {
		columns.add(p);
		states.add(current);

		//if (!target.isEmpty()){ // TODO verify this if is not needed
			rows.get(current.getId()).addReduce( p,  target);
		//}
		
	}

	// reduce all row
	public void addReduceAll(ItemState current) {
		states.add(current);
		LookupTableRow row = rows.get(current.getId());
		
		for (Production p : columns){
			for (ProductionItem item : current){
				row.addReduce(p , item);
			}
		}
		
	}
	
	public void addShift(ItemState current, Production p, ItemState canonical) {
		columns.add(p);
		states.add(current);
		rows.get(current.getId()).addShift( p,  canonical);
	}

	public void addGoto(ItemState current, Production p, ItemState canonical) {
		columns.add(p);
		states.add(current);
		rows.get(current.getId()).addGoto( p,  canonical);
	}

	public void addAccept(ItemState current, Production p) {
		columns.add(p);
		states.add(current);
		rows.get(current.getId()).addAccept(p,current);
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
				LookupTableAction action = r.actionsFor(p);
				
				builder.append(action.toString());
			
				
				builder.append("\t");
			}
			builder.append("\n");
		}
		
		return builder.toString();
	}

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
	
	public Stream<LookupTableRow> stream(){
		return this.rows.stream();
	}

	

}
