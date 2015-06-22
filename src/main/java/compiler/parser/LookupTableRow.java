package compiler.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import compiler.lexer.Token;

public class LookupTableRow implements Iterable<Map.Entry<Production, LookupTableAction>>{

    Map<Production, LookupTableAction> dictionary  = new HashMap<Production, LookupTableAction>();
    int id;
	private LookupTable table;
	
	public LookupTableRow(LookupTable table, int id) {
		this.id = id;
		this.table = table;
	}
	

	public boolean equals(LookupTableRow other){
		return this.id == other.id && this.dictionary.size() == other.dictionary.size();
	}
	
	public int getId(){
		return id;
	}
	
	public String toString(){
		return Integer.toString(id);
	}
	
	public Stream<Map.Entry<Production, LookupTableAction>> stream(){
		return dictionary.entrySet().stream();
	}
	
	private void addAction(Production p, LookupTableAction action)
	{
		LookupTableAction previousAction = dictionary.get(p);

		
		if (previousAction != null){
			SplitAction split = SplitAction.split(previousAction, action);
			dictionary.put(p, split);
		} else {
			dictionary.put(p,action);
		}
		
		
	}

	public void addReduce(Production p, int productionItemTargetId) {
		
		addAction(p, new ReduceAction(productionItemTargetId,table));
	}
	
	
	public void addShift(Production p, ItemState canonical) {
		addShift(p, canonical.getId());
	}
	
	public LookupTableRow addShift(Production p, int stateId ) {
		addAction(p, new ShiftAction(stateId));
		return this;
	}

	public void addGoto(Production p, ItemState canonical) {
		addGoto(p, canonical.getId());
	}
	
	public LookupTableRow addGoto(Production p, int stateId) {
		addAction(p, new GotoAction(stateId));
		return this;
	}

	public void addAccept(Production p,ItemState canonical) {
		addAction(p, new AcceptAction());
	}

	public LookupTableAction matchWith(StackItem peek) 
	{
		LookupTableAction action = listMatchWith(peek);
		 
		if (action == null){
			return new ExceptionAction(id);
		} else {
			return action;
		}
		
	}
	
    private LookupTableAction listMatchWith(StackItem peek) 
	{
		
		if (peek instanceof TokenStackItem)
		{
			for(Map.Entry<Production, LookupTableAction> entry : dictionary.entrySet() )
			{
				
				if (match(entry.getKey() , ((TokenStackItem)peek).getToken()))
				{
					return entry.getValue();
				}
				
			}
			
		} else if (peek instanceof ProductionStackItem){
			for(Map.Entry<Production, LookupTableAction> entry : dictionary.entrySet() )
			{
				
				if (match(entry.getKey() , ((ProductionStackItem)peek).getProduction()))
				{
					return entry.getValue();
				}
				
			}
		}

		return new ExceptionAction(id);
	}
	
	private boolean match (Production p , Production t)
	{
		if (p == t){
			return true;
		} else if (p.isNonTerminal() && t.isNonTerminal()){
			
			if (p.getLabel().equals(t.getLabel())){
				return (p.toNonTerminal().getRule().equals(t.toNonTerminal().getRule()));
			}
			
		} 
		return false;
		
	}
	
	private boolean match (Production p , Token t)
	{
		if (p.isTerminal()){
			return p.toTerminal().match(t);
		} else if (p.isAutoNonTerminal()){
			return ((AutoNonTerminal)p).match(t);
		} else {
			return false;
		}
	}

	public LookupTableAction  getActionFor(Production p) {
		LookupTableAction  action = dictionary.get(p);
		
		if (action == null){
			return new ExceptionAction(id);
		}
		
		return action;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Entry<Production, LookupTableAction>> iterator() {
		return dictionary.entrySet().iterator();
	}




}
