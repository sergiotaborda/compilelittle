/**
 * 
 */
package compiler.parser;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class ProductionTransversor {

	
	public static void transverse(Production p , ProductionVisitor visitor){
		
		Set<NonTerminal> visited = new HashSet<>();

		if (visitor.startVisit() == VisitorNext.Children){
			visit(p, null, visitor, visited);
			visitor.endVisit();
		}
		
	}

	private static VisitorNext visit(Production p, Production parent , ProductionVisitor visitor, Set<NonTerminal> visited) {
		if (p.isTerminal()){
			Terminal t = (Terminal)p;
			return visitor.visitTerminal(t, parent);
			
		} else if (p.isSequence()){
			ProductionSequence s = (ProductionSequence)p;
			if (visitor.visitBeforeSequence(s, parent) == VisitorNext.Children){
				for(Production pp : s){
					if (visit(pp, s, visitor, visited) != VisitorNext.Siblings){
						break;
					}
				}
				return visitor.visitAfterSequence(s, parent);
			}
			return VisitorNext.Siblings;
		
		} else if (p.isMultiple()){
			ProductionMultiple s = (ProductionMultiple)p;
			
			if (visitor.visitBeforeMultiple(s,parent) == VisitorNext.Children){
				visit(s.getTemplate(),s , visitor, visited);
				return visitor.visitAfterMultiple(s,parent);
			}
			return VisitorNext.Siblings;
		} else if (p.isAlternative()){
			
			ProductionAlternative s = (ProductionAlternative)p;
			
			if (visitor.visitBeforeAlternative(s,parent) == VisitorNext.Children){
				for(Production pp : s){
					if (visit(pp, s, visitor, visited) != VisitorNext.Siblings){
						break;
					}		
				}
				return visitor.visitAfterAlternative(s,parent);
			}
			return VisitorNext.Siblings;
		} else if (p.isNonTerminal()){

			NonTerminal n = (NonTerminal)p;

			if (visited.add(n)){
				
				if (visitor.visitBeforeNonTerminal(n, parent) == VisitorNext.Children){
					
					Production rule = n.getRule();
					if (rule == null){
						throw new RuntimeException("Rule is null for nontermial " + n.getName());
					}

					if (rule != null){
						visit(rule, n, visitor, visited);
					}
					return visitor.visitAfterNonTerminal(n, parent);
				}
				return VisitorNext.Siblings;
			} else {
				return visitor.visitRepeatNonTerminal(n, parent);
			}

		} else if (p.isAutoNonTerminal()){
			AutoNonTerminal n = (AutoNonTerminal)p;
			return visitor.visitBeforeAutoNonTerminal(n, parent);
		} else {
			throw new IllegalStateException("Not recognized production " + p);
		}
	}
}
