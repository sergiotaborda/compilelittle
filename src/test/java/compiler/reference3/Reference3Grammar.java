/**
 * 
 */
package compiler.reference3;

import java.util.Set;

import compiler.AbstractGrammar;
import compiler.parser.EmptyTerminal;
import compiler.parser.NonTerminal;
import compiler.parser.Terminal;

/**
 * 
 */
public class Reference3Grammar extends AbstractGrammar {

	/**
	 * S -> B b | C d
	 * B -> a B | £
	 * C -> c C | £
	 *  
	 */
	@Override
	protected NonTerminal defineGrammar() {
		NonTerminal S = NonTerminal.of("S");
		NonTerminal B = NonTerminal.of("B");
		NonTerminal C = NonTerminal.of("C");
		
		Terminal a = Terminal.of("a");
		Terminal b = Terminal.of("b");
		Terminal c = Terminal.of("c");
		Terminal d = Terminal.of("d");

		
		S.setRule(B.add(b).or(C.add(d)));
		B.setRule(a.add(B).or(EmptyTerminal.instance()));
		C.setRule(c.add(C).or(EmptyTerminal.instance()));
		
		return S;
	}

	@Override
	public boolean isIgnore(char c) {
		return c != 'a' && c != 'b' && c != 'c' && c != 'd';
	}
	
	protected void addStopCharacters(Set<Character> stopCharacters) {
		stopCharacters.add('a');
		stopCharacters.add('b');
		stopCharacters.add('c');
		stopCharacters.add('d');
	}

}
