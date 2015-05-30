/**
 * 
 */
package compiler.parser;

import compiler.FirstFollowTable;
import compiler.Grammar;
import compiler.RealizedPromisseSet;

/**
 * 
 */
public class SLRAutomaton extends AbstractAutomaton {


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RealizedPromisseSet<MatchableProduction> calculateAhead(ItemState state, ProductionItem p) {
		return new RealizedPromisseSet<>();
	}

	protected void createReduceActions(Grammar grammar,FirstFollowTable firstFollowTable, LookupTable table, ItemState current) {
		for (ProductionItem item : current){
			if (item.isFinal()){
				RealizedPromisseSet<MatchableProduction> followSet;
				if (item.root.getLabel().endsWith("'")){
					followSet = new RealizedPromisseSet<MatchableProduction>(EOFTerminal.instance());
					
				} else {
					followSet = firstFollowTable.followOf(item.root);
				}
			
				if (followSet.isEmpty()){
					throw new RuntimeException("Follow is empty for production " + item.root);
				}
				for(MatchableProduction m : followSet){
					table.addReduce(current , m , item); 
				}


			} 
		}
	}


}
