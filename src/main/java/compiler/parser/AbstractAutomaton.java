/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import compiler.FirstFollowTable;
import compiler.FirstFollowTableCalculator;
import compiler.Grammar;
import compiler.RealizedPromisseSet;


/**
 * 
 */
public abstract class AbstractAutomaton implements ItemStateAutomaton {


	/**
	 * {@inheritDoc}
	 */
	@Override
	public LookupTable produceLookupTable(Grammar grammar) {

		FirstFollowTable firstFollowTable = new FirstFollowTableCalculator().calculateFrom(grammar.getStartProduction());

		ClosureCalculator closureCalculator = new CachedClosureCalculator();
		List<ItemState> states = calculateStates(grammar, firstFollowTable, closureCalculator);

		recountStates(states);

		return createTable( grammar,firstFollowTable,states);
	}


	private LookupTable createTable(Grammar grammar,FirstFollowTable firstFollowTable, List<ItemState> states) {
		LookupTable table = new LookupTable(states);

		for(ItemState current : states){
			Set<Entry<Production, ItemState>> transformations = current.getTransformations();

			if (!transformations.isEmpty()){
				// is possible to go to other states
				// shift 

				for(Entry<Production, ItemState> t : transformations){

					ItemState canonical = t.getValue();
					Production trigger = t.getKey();

					if (trigger.isTerminal() || trigger.isAutoNonTerminal()){
						// shift
						table.addShift(current , trigger , canonical);
					} else {
						table.addGoto(current , trigger , canonical);
					}
				}
			}

			// can also has reduce states

			if (current.containsAugmented()){
				table.addAccept(current , EOFTerminal.instance() );
			} else {

				createReduceActions(grammar,firstFollowTable, table, current);

			}


		}
		return table;
	}


	protected void createReduceActions(Grammar grammar,FirstFollowTable firstFollowTable, LookupTable table, ItemState current) {
		for (ProductionItem item : current){
			if (item.isFinal()){

				if (item.getLookAhead().isEmpty()){
					table.addReduceAll(current); 
				} else {
					for(MatchableProduction m : item.getLookAhead()){
						table.addReduce(current , m , item); 
					}
				}

			} 
		}
	}


	private void recountStates(List<ItemState> states) {

		states.sort( (x, y) -> x.getId() - y.getId());

		int count = 0;
		for(ItemState s : states){
			s.setId (count++);
		}
	}

	protected final List<ItemState> calculateStates(Grammar grammar, FirstFollowTable firstFollowTable,
			ClosureCalculator closureCalculator) {
		ItemStatesCollection states = new ItemStatesCollection();
		ItemState state  = initialStateOf(grammar,closureCalculator, firstFollowTable);
		states.add(state);

		Queue<ItemState>  deque = new LinkedList<>();
		deque.add(state);



		while (!deque.isEmpty()){
			ItemState current;

			current = deque.remove();

			Iterator<Production> it = current.getNextObservables().iterator();
			while (it.hasNext()){
				Production p = it.next();
				advanceState(states, deque, current, p,closureCalculator);
			}

		}

		return states.toList();
	}


	protected ItemState advanceWith(ItemState state, Production p,ClosureCalculator closureCalculator) {
		ItemState ns = new ItemState(state.table);

		state.items.stream().filter(item -> !item.isFinal() && item.getNextReadable().equals(p)).forEach(item -> {
			ns.add(item.advance());
		});

		expandCanonical(ns,closureCalculator);
		return ns;
	}


	protected void advanceState(ItemStatesCollection cannonicalStates, Queue<ItemState> deque, ItemState current, Production p,ClosureCalculator closureCalculator) {


			ItemState newState  = advanceWith(current,p,closureCalculator);

			Optional<ItemState> maybeCannonical = cannonicalStates.find(newState);
			
			if (maybeCannonical.isPresent()){
				// cannonical found
				current.addStateTransform(p, maybeCannonical.get());
			} else {
				// new cannonical found, newState is cannonical

				cannonicalStates.add(newState);
				current.addStateTransform(p, newState);
				
				if (!newState.isFinal()){
					deque.add(newState);
				} else {
					// final
					newState.isFinal();
				}
				
			}

	
	}

	protected ItemState initialStateOf(Grammar grammar,ClosureCalculator closureCalculator,FirstFollowTable firstFollowTable) {

		Production start = grammar.getStartProduction();


		NonTerminal augmented = new NonTerminal(start.getLabel() + "'");
		augmented.setRule(start);
		augmented.actions = start.getSemanticActions();

		ItemState state = new ItemState(firstFollowTable);

		ProductionItem a = createFirstItem();

		a.root = augmented;
		a.productions.add(start);
		a.isAugmented = true;
		a.semanticActions = augmented.actions;

		state.add(a);

		expandCanonical(state,closureCalculator);
		return state;
	}

	protected ProductionItem createFirstItem(){
		return new ProductionItem(EOFTerminal.instance());
	}




	protected void expandCanonical(ItemState state,ClosureCalculator closureCalculator){

		List<ProductionItem> previousDiscovered = state.items;
		List<ProductionItem> newDiscovered = new LinkedList<>();
		do{
			for (ProductionItem p : previousDiscovered){
				if (!p.isFinal()){
					Production next = p.getNextReadable();
					if (next.isNonTerminal()){
						RealizedPromisseSet<MatchableProduction> ahead = calculateAhead(state,p);

						for ( ProductionItem m : closureCalculator.closureOf(next, ((NonTerminal)next).getRule() , ahead)){
							if (!state.uniqueItems.contains(m)){
								if ( m.getNextReadable() != null && m.getNextReadable().isNonTerminal()){
									newDiscovered.add(m);
								}
								state.add(m);
							}
						}
					}
				}
			}
			previousDiscovered = newDiscovered;
			newDiscovered = new LinkedList<>();

		} while (!previousDiscovered.isEmpty());

		state.mergeItems();
	}

	protected abstract RealizedPromisseSet<MatchableProduction> calculateAhead(ItemState state,ProductionItem p);
}
