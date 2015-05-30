/**
 * 
 */
package compiler.parser;

import java.util.Iterator;

import compiler.PromisseSet;
import compiler.RealizedPromisseSet;

/**
 * 
 */
class LALRAutomaton extends AbstractAutomaton  {



	protected RealizedPromisseSet<MatchableProduction> calculateAhead(ItemState state,ProductionItem p) {

		Iterator<Production> iterator = p.getAllAfterNextReadable();

		if (iterator.hasNext()){

			RealizedPromisseSet<MatchableProduction> ahaed = new RealizedPromisseSet<MatchableProduction>();
			while (iterator.hasNext()){
				Production afterNext = iterator.next();

				// get the firstSet of the next production
				PromisseSet<MatchableProduction> firstSet =  state.table.firstOf(afterNext);
				if (firstSet.contains(EmptyTerminal.instance())){
					// if it contains an empty production, repeat the process
					ahaed = ahaed.union(firstSet.subtract(EmptyTerminal.instance()));
				} else {
					ahaed = ahaed.union(firstSet);
					break;
				}	
			}

			return ahaed.simplify();
		} else {
			// no more productions
			return p.getLookAhead().subtract(EmptyTerminal.instance()).simplify();
		}


	}
	


}
