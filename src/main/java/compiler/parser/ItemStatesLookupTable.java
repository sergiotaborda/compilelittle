package compiler.parser;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import compiler.Grammar;

public class ItemStatesLookupTable extends AbstractLookupTable {

	protected Set<ItemState> states = new LinkedHashSet<>();

	public ItemStatesLookupTable(Grammar grammar,List<ItemState> states) {
		super(grammar, states.size());
		
		for (ItemState s : states){
			rows.add(new LookupTableRow(this,s.getId()));
		}
		
	}
	
	public List<ItemState> getStates() {
		ArrayList<ItemState> list = new ArrayList<>(states);
		list.sort( (a , b) -> a.getId() - b.getId());
		return list;
	}

	public void addReduce(ItemState current, Production p, ProductionItem target ) {
		columns.add(p);
		states.add(current);

		if (target.id == null){
			target.id = getGrammar().getFinalProductionItemTargetId(target);
		} 

		rows.get(current.getId()).addReduce( p,  target.id);
	}

	// reduce all row
	public void addReduceAll(ItemState current) {
		states.add(current);
		LookupTableRow row = rows.get(current.getId());
		
		for (Production p : columns){
			for (ProductionItem item : current){
				
				if (item.id == null){
					item.id = getGrammar().getFinalProductionItemTargetId(item);
				} 
			
				row.addReduce(p , item.id);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductionItem getFinalProductionItem(int targetId) {
		return getGrammar().getFinalProductionItem(targetId);
	}





	

}
