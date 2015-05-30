/**
 * 
 */
package compiler.parser;

import compiler.RealizedPromisseSet;

/**
 * 
 */
public class LRZeroAutomaton extends AbstractAutomaton  {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected RealizedPromisseSet<MatchableProduction> calculateAhead(
			ItemState state, ProductionItem p) {
		return new RealizedPromisseSet<MatchableProduction>();
	}

	protected ProductionItem createFirstItem(){
		return new ProductionItem(new RealizedPromisseSet<MatchableProduction>());
	}
}
