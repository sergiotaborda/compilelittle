/**
 * 
 */
package compiler.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.RealizedPromisseSet;

/**
 * 
 */
public class TestItemState {

	@Test
	public void test() {
		ItemState s1 = new ItemState(null);
		ItemState s2 = new ItemState(null);
		
		NonTerminal n = new NonTerminal("A");
	
		ProductionItem p = new ProductionItem(new RealizedPromisseSet<>(Terminal.of("["), Terminal.of("]")));
		
		p.root  = n;
		p.productions.add(Terminal.of("("));
		p.productions.add(n);
		p.productions.add(Terminal.of(")"));
		
		ProductionItem p2 = new ProductionItem(new RealizedPromisseSet<>(Terminal.of("{"), Terminal.of("}")));
		
		p2.root  = n;
		p2.productions.add(Terminal.of("("));
		p2.productions.add(n);
		p2.productions.add(Terminal.of(")"));
		
		s1.add(p);
		s1.add(p2);
		
		s1.mergeItems();
		
		s2.add(p);
		s2.add(p2);
		
		s2.mergeItems();
		
		assertTrue(s1.itemsEquals(s1));
		assertTrue(s1.itemsEquals(s2));
	}

}
