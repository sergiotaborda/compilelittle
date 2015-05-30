/**
 * 
 */
package compiler.reference4;

import java.util.Set;

import compiler.AbstractGrammar;
import compiler.parser.NonTerminal;
import compiler.parser.Terminal;

/**
 * LR(1) only grammar
 * 
 * R -> S S 
 * S  -> C C 
 * C  -> c C | d
 */
public class Reference4Grammar extends AbstractGrammar {

	@Override
	protected NonTerminal defineGrammar() {
		NonTerminal R = NonTerminal.of("R");
		NonTerminal S = NonTerminal.of("S");
		NonTerminal C = NonTerminal.of("C");
		
		Terminal c = Terminal.of("c");
		Terminal d = Terminal.of("d");

		R.setRule(S.add(S));
		S.setRule(C.add(C));
		C.setRule(c.add(C).or(d));
		
		return S;
	}

	@Override
	public boolean isIgnore(char c) {
		return c != 'c' && c != 'd';
	}
	
	protected void addStopCharacters(Set<Character> stopCharacters) {
		stopCharacters.add('c');
		stopCharacters.add('d');
	}

}
