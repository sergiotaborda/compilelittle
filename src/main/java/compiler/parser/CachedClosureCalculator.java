/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compiler.RealizedPromisseSet;

/**
 * 
 */
public class CachedClosureCalculator implements ClosureCalculator {

	Map<Production, List<ProductionItem>> cache = new HashMap<Production, List<ProductionItem>>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ProductionItem> closureOf(Production root ,Production production, RealizedPromisseSet<MatchableProduction> lookAhead){
		return calculateClosure(root, production, lookAhead);
//		List<ProductionItem> cached = cache.get(production);
//		
//		if (cached == null){
//			cached = calculateClosure(root, production, lookAhead);
//			cache.put(production, cached);
//			
//			return cached;
//		} else {
//			List<ProductionItem> res = new ArrayList<ProductionItem>(cached.size());
//			
//			for (ProductionItem item : cached){
//				res.add(item.relook(lookAhead));
//			}
//			
//			return res;
//		}
	}
	
	private List<ProductionItem> calculateClosure(Production root ,Production production, RealizedPromisseSet<MatchableProduction> lookAhead){


		if (production.isTerminal()){
			ProductionItem item = new ProductionItem(lookAhead);
			item.root = root;
			item.productions.add(production);
			item.semanticActions = production.getSemanticActions();
			return Collections.singletonList(item);
		} else if (production.isSequence()){			
			ProductionItem item = new ProductionItem(lookAhead);
			item.root = root;
			item.semanticActions = production.getSemanticActions();
			if (item.semanticActions.isEmpty()){
				item.semanticActions = root.getSemanticActions();
			}
			for (Production p : production.toSequence()){
				item.productions.add(p);
			}
			return Collections.singletonList(item);
		} else if (production.isAlternative()){			

			List<ProductionItem> res = new ArrayList<>();
			for (Production p : production.toAlternative()){

				if (p.isNonTerminal()){
					ProductionItem item = new ProductionItem(lookAhead);
					item.root = root;
					item.productions.add(p);
					item.semanticActions = p.getSemanticActions();

					res.add(item);
				} else if (p.isEmpty()){
					ProductionItem item = new ProductionItem(lookAhead);
					item.root = root;
					res.add(item);
//				} else if (p.isSequence()){ // CHANGED
//					Production f = p.toSequence().get(0);
//					if (f != root){
//						res.addAll(closure(root, p,lookAhead));
//					}
				} else {
					res.addAll(closureOf(root, p,lookAhead));
				}

			}
			return res;
		} else if (production.isAutoNonTerminal()){
			ProductionItem item = new ProductionItem(lookAhead);
			item.root = root;
			item.productions.add(production);
			item.semanticActions = production.getSemanticActions();
			return Collections.singletonList(item);
		} else if (production.isNonTerminal()){

			Production rule = production.toNonTerminal().getRule();
			if (rule.isTerminal() || rule.isAutoNonTerminal()){
				ProductionItem item = new ProductionItem(lookAhead);
				item.root = root;
				item.productions.add(production);
				item.semanticActions = production.getSemanticActions();
				return Collections.singletonList(item);
			} else {
				return closureOf( production , production.toNonTerminal().getRule() , lookAhead);
			}

		} else {
			throw new RuntimeException("Production not recognized");
		}
	}
}
