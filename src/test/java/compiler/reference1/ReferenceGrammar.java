package compiler.reference1;

import java.util.Set;

import compiler.AbstractGrammar;
import compiler.parser.NonTerminal;
import compiler.parser.ProductionSequence;
import compiler.parser.Terminal;

public class ReferenceGrammar extends AbstractGrammar{

	/**
	 * 
	 * S -> A A
	 * A -> a A | b
	 */
	@Override
	protected NonTerminal defineGrammar() {
		NonTerminal S = NonTerminal.of("S");
		NonTerminal A = NonTerminal.of("A");
		
		Terminal a = Terminal.of("a");
		a.addSemanticAction( (p, r) -> {
			
			p.setSemanticAttribute("node", new Node((String)r.get(0).getSemanticAttribute("lexicalValue").get()));
			
		});
		Terminal b = Terminal.of("b");
		b.addSemanticAction( (p, r) -> {
			
			
			p.setSemanticAttribute("node", new Node((String)r.get(0).getSemanticAttribute("lexicalValue").get()));
			
		});
		S.setRule(A.add(A));
		S.addSemanticAction( (p, r) -> {
			
			Node n = new Node();
			
			n.add((Node)r.get(0).getSemanticAttribute("node").get());
			n.add((Node)r.get(1).getSemanticAttribute("node").get());
			
			p.setSemanticAttribute("node", n);
			
		});
		
		ProductionSequence A1 = a.add(A);
		
		A1.addSemanticAction( (p, r) -> {
			
			Node n = new Node();
			
			n.add(new Node((String)r.get(0).getSemanticAttribute("lexicalValue").get()));
			n.add((Node)r.get(1).getSemanticAttribute("node").get());
			
			p.setSemanticAttribute("node", n);
			
		});
		
		A.setRule(A1.or(b));
		A.addSemanticAction( (p, r) -> {
			
			Node n = new Node();
	
			
		});
		return S;
	}

	@Override
	public boolean isIgnore(char c) {
		return c != 'a' && c != 'b';
	}
	
	protected void addStopCharacters(Set<Character> stopCharacters) {
		stopCharacters.add('a');
		stopCharacters.add('b');
	}


}
