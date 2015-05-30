package compiler.reference3;

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

public class TestReference3Grammar {

	
	
	@Test
	public void testFirstAndFollow() {

		Reference3Grammar g = new Reference3Grammar();
		
		FirstFollowTable table = new FirstFollowTableCalculator().calculateFrom(g.getStartProduction());
		
		assertNotNull(table);
		
		NonTerminal S = g.getStartProduction().toNonTerminal();
		NonTerminal B = g.getStartProduction().toNonTerminal().getRule().toAlternative().get(0).toSequence().get(0).toNonTerminal();
		NonTerminal C = g.getStartProduction().toNonTerminal().getRule().toAlternative().get(1).toSequence().get(0).toNonTerminal();

		// first
		RealizedPromisseSet<MatchableProduction> first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("a"), Terminal.of("b"),Terminal.of("c"),Terminal.of("d"));
		 
		assertEquals(first,  table.firstOf(S));
		
		
		first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("a"), EmptyTerminal.instance());
		 
		assertEquals(first,  table.firstOf(B));
		
		first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("c"),EmptyTerminal.instance());
		 
		assertEquals(first,  table.firstOf(C));
		
		// follow
		
		RealizedPromisseSet<MatchableProduction> follow = new RealizedPromisseSet<MatchableProduction>(EOFTerminal.instance());
		 
		assertEquals(follow,  table.followOf(S));

		
		follow = new RealizedPromisseSet<MatchableProduction>(Terminal.of("b"));
		 
		assertEquals(follow,  table.followOf(B));
		
		follow = new RealizedPromisseSet<MatchableProduction>(Terminal.of("d"));
		 
		assertEquals(follow,  table.followOf(C));

		

	}

	
}
