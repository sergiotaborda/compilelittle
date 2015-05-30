package compiler.reference2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import compiler.FirstFollowTable;
import compiler.FirstFollowTableCalculator;
import compiler.RealizedPromisseSet;
import compiler.parser.EOFTerminal;
import compiler.parser.EmptyTerminal;
import compiler.parser.MatchableProduction;
import compiler.parser.NonTerminal;
import compiler.parser.Terminal;

public class TestReference2Grammar {

	
	
	@Test
	public void testFirstAndFollow() {

		Reference2Grammar g = new Reference2Grammar();
		
		FirstFollowTable table = new FirstFollowTableCalculator().calculateFrom(g.getStartProduction());
		
		assertNotNull(table);
		
		NonTerminal S = g.getStartProduction().toNonTerminal();
		NonTerminal A = g.getStartProduction().toNonTerminal().getRule().toSequence().get(0).toNonTerminal();
		NonTerminal B = g.getStartProduction().toNonTerminal().getRule().toSequence().get(1).toNonTerminal();
		NonTerminal C = g.getStartProduction().toNonTerminal().getRule().toSequence().get(2).toNonTerminal();
		NonTerminal D = g.getStartProduction().toNonTerminal().getRule().toSequence().get(3).toNonTerminal();
		NonTerminal E = g.getStartProduction().toNonTerminal().getRule().toSequence().get(4).toNonTerminal();
		
		// first
		RealizedPromisseSet<MatchableProduction> first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("a"), Terminal.of("b"),Terminal.of("c"));
		 
		assertEquals(first,  table.firstOf(S));
		
		first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("a"), EmptyTerminal.instance());
		 
		assertEquals(first,  table.firstOf(A));
		
		first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("b"), EmptyTerminal.instance());
		 
		assertEquals(first,  table.firstOf(B));
		
		first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("c"));
		 
		assertEquals(first,  table.firstOf(C));
		
		first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("d"), EmptyTerminal.instance());
		 
		assertEquals(first,  table.firstOf(D));
		
		first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("e"), EmptyTerminal.instance());
		 
		assertEquals(first,  table.firstOf(E));
		
		// follow
		
		RealizedPromisseSet<MatchableProduction> follow = new RealizedPromisseSet<MatchableProduction>(EOFTerminal.instance());
		 
		assertEquals(follow,  table.followOf(S));
		assertEquals(follow,  table.followOf(E));
		
		follow = new RealizedPromisseSet<MatchableProduction>(Terminal.of("b"),Terminal.of("c"));
		 
		assertEquals(follow,  table.followOf(A));
		
		follow = new RealizedPromisseSet<MatchableProduction>(Terminal.of("c"));
		 
		assertEquals(follow,  table.followOf(B));
		
		follow = new RealizedPromisseSet<MatchableProduction>(Terminal.of("d"),Terminal.of("e"), EOFTerminal.instance() );
		 
		assertEquals(follow,  table.followOf(C));
		
		follow = new RealizedPromisseSet<MatchableProduction>(Terminal.of("e"), EOFTerminal.instance() );
		 
		assertEquals(follow,  table.followOf(D));
		

	}

	
}
