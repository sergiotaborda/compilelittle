/**
 * 
 */
package compiler.reference2;

import java.util.Set;

import compiler.AbstractGrammar;
import compiler.parser.EmptyTerminal;
import compiler.parser.NonTerminal;
import compiler.parser.ProductionSequence;
import compiler.parser.Terminal;
import compiler.reference1.Node;

/**
 * 
 */
public class Reference2Grammar extends AbstractGrammar {

	/***
	 * 
	 * S- > A B C D E
	 * A -> a | £
	 * B -> b | £
	 * C -> c
	 * D -> d | £
	 * E -> e | £
	 */
	@Override
	protected NonTerminal defineGrammar() {
		NonTerminal S = NonTerminal.of("S");
		NonTerminal A = NonTerminal.of("A");
		NonTerminal B = NonTerminal.of("B");
		NonTerminal C = NonTerminal.of("C");
		NonTerminal D = NonTerminal.of("D");
		NonTerminal E = NonTerminal.of("E");
		
		Terminal a = Terminal.of("a");
		Terminal b = Terminal.of("b");
		Terminal c = Terminal.of("c");
		Terminal d = Terminal.of("d");
		Terminal e = Terminal.of("e");
		
		S.setRule(A.add(B).add(C).add(D).add(E));
		A.setRule(a.or(EmptyTerminal.instance()));
		B.setRule(b.or(EmptyTerminal.instance()));
		C.setRule(c);
		D.setRule(d.or(EmptyTerminal.instance()));
		E.setRule(e.or(EmptyTerminal.instance()));
		
		return S;
	}

	@Override
	public boolean isIgnore(char c) {
		return c != 'a' && c != 'b' && c != 'c' && c != 'd' && c != 'e';
	}
	
	protected void addStopCharacters(Set<Character> stopCharacters) {
		stopCharacters.add('a');
		stopCharacters.add('b');
		stopCharacters.add('c');
		stopCharacters.add('d');
		stopCharacters.add('e');
	}

}
