/**
 * 
 */
package compiler;

import compiler.parser.EmptyTerminal;
import compiler.parser.MatchableProduction;
import compiler.parser.NonTerminal;
import compiler.parser.Production;
import compiler.parser.ProductionAlternative;
import compiler.parser.ProductionMultiple;
import compiler.parser.ProductionSequence;
import compiler.parser.ProductionVisitor;
import compiler.parser.Terminal;
import compiler.parser.VisitorNext;

/**
 * 
 */
public class CalculateFirstProductionWalker extends ProductionVisitor {


	
	private MappedFirstFollowTable table;

	public CalculateFirstProductionWalker(){
		
	}
	/**
	 * Constructor.
	 * @param table
	 */
	public CalculateFirstProductionWalker(MappedFirstFollowTable table) {
		this.table = table;
	}
	
	/**
	 * 
	 */
	protected VisitorNext startVisit() {
		return VisitorNext.Children;
	}
	
	protected VisitorNext visitTerminal(Terminal t, Production parent){
		
		table.addFirst(t, t);
		
		return super.visitTerminal(t, parent);
		
	}
	
	/**
	 * @param n
	 */
	protected VisitorNext visitAfterNonTerminal(NonTerminal n, Production parent) {
		
		table.addFirst(n, table.firstOf(n.getRule()));
		
		return  super.visitAfterNonTerminal(n, parent);
		
	}
	
	/**
	 * @param s
	 */
	protected VisitorNext visitAfterAlternative(ProductionAlternative s, Production parent) {

		for (Production p : s){
			table.addFirst(s, table.firstOf(p));
		}

		return  super.visitAfterAlternative(s, parent);
	}
	
	/**
	 * @param s
	 */
	protected VisitorNext visitAfterSequence(ProductionSequence s, Production parent) {

		table.addFirst(s, calculateFirstInSequence(s,0));
		
		return  super.visitAfterSequence(s, parent);
	}
	
	private PromisseSet<MatchableProduction> calculateFirstInSequence(ProductionSequence seq, int startIndex){

		if (startIndex >= seq.size()){
			return new EmptyPromisseSet<MatchableProduction>();
		}
		Production item = seq.get(startIndex);


		PromisseSet<MatchableProduction> set = table.firstOf(item);

		if (set.contains(EmptyTerminal.instance())) {
			set = set.subtract(EmptyTerminal.instance());

			// because can evaluate to empty, get the first of the next item

			PromisseSet<MatchableProduction> nextset = calculateFirstInSequence(seq, startIndex + 1);
			set = set.union(nextset);

		} 
		return set;
	}
	

}
