/**
 * 
 */
package compiler.parser;

import java.util.List;

import compiler.RealizedPromisseSet;

/**
 * 
 */
public interface ClosureCalculator {

	public List<ProductionItem> closureOf(Production root,
			Production production,
			RealizedPromisseSet<MatchableProduction> lookAhead);

}