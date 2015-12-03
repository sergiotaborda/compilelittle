package compiler.reference1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import compiler.AstCompiler;
import compiler.CompiledUnit;
import compiler.FirstFollowTable;
import compiler.FirstFollowTableCalculator;
import compiler.ListCompilationUnitSet;
import compiler.RealizedPromisseSet;
import compiler.StringCompilationUnit;
import compiler.parser.EOFTerminal;
import compiler.parser.LALRAutomatonFactory;
import compiler.parser.LookupTable;
import compiler.parser.MatchableProduction;
import compiler.parser.Production;
import compiler.parser.Terminal;

public class TestReferenceGrammar {

	
	
	@Test
	public void testFirstAndFollow() {

		ReferenceGrammar g = new ReferenceGrammar();
		
		
		FirstFollowTable table = new FirstFollowTableCalculator().calculateFrom(g.getStartProduction());
		
		final Production S = g.getStartProduction(); 
		final Production A = S.toNonTerminal().getRule().toSequence().getFirst();
		
		assertNotNull(table);
		RealizedPromisseSet<MatchableProduction> first = new RealizedPromisseSet<MatchableProduction>(Terminal.of("a"), Terminal.of("b"));
		 
		assertEquals(first,  table.firstOf(S));
		assertEquals(first,  table.firstOf(A));
		
		RealizedPromisseSet<MatchableProduction> follow = new RealizedPromisseSet<MatchableProduction>(EOFTerminal.instance());
		 
		assertEquals(follow,  table.followOf(S));
		
		follow = new RealizedPromisseSet<MatchableProduction>(Terminal.of("a"), Terminal.of("b"), EOFTerminal.instance());
		
		
		assertEquals(follow,  table.followOf(A));
	}

	@Test
	public void testLookupTable() throws IOException {


		ReferenceGrammar g = new ReferenceGrammar();
		
		LookupTable table = new LALRAutomatonFactory().create().produceLookupTable(g);
		
		System.out.println(table);
		
		assertNotNull(table);
	}
	
	
	@Test 
	public void testCompiler() throws IOException {

		String text = "aaabb";
		
		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit(text));

	
		final AstCompiler compiler = new AstCompiler(new ReferenceLanguage());
		
		List<CompiledUnit> nodes = compiler.parse(unitSet).sendToList();
		
		assertEquals(1, nodes.size());
		
		assertNotNull(nodes.get(0));

	}
}
