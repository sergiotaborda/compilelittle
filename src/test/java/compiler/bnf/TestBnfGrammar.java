package compiler.bnf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import compiler.AstCompiler;
import compiler.FileCompilationUnit;
import compiler.FirstFollowTable;
import compiler.FirstFollowTableCalculator;
import compiler.ListCompilationUnitSet;
import compiler.RealizedPromisseSet;
import compiler.parser.EOFTerminal;
import compiler.parser.Identifier;
import compiler.parser.LookupTable;
import compiler.parser.MatchableProduction;
import compiler.parser.NonTerminal;
import compiler.parser.Production;
import compiler.parser.Terminal;
import compiler.parser.Text;

public class TestBnfGrammar {

	@Test
	public void testFirstAndFollow() {

		EBnfGrammar g = new EBnfGrammar();
		
		FirstFollowTable table = new FirstFollowTableCalculator().calculateFrom(g.getStartProduction());
		
		assertNotNull(table);
		
		assertFalse(table.firstOf(new NonTerminal(",")).isEmpty());
		
		Production rules =  g.getStartProduction();
		Production rule = rules.toNonTerminal().getRule().toAlternative().get(1).toSequence().get(0).toNonTerminal();
		Production ruleName = rule.toNonTerminal().getRule().toSequence().get(0).toNonTerminal();
		Production expression = rule.toNonTerminal().getRule().toSequence().get(2).toNonTerminal();
		Production terms = expression.toNonTerminal().getRule().toAlternative().get(0).toNonTerminal();
		Production term = terms.toNonTerminal().getRule().toAlternative().get(0).toNonTerminal();
		Production ruleRef = term.toNonTerminal().getRule().toAlternative().get(0).toNonTerminal();
		Production identifier = term.toNonTerminal().getRule().toAlternative().get(1).toNonTerminal();
		
		RealizedPromisseSet<MatchableProduction> first = new RealizedPromisseSet<MatchableProduction>(Identifier.instance());
		 
		assertEquals(first,  table.firstOf(rules));
		assertEquals(first,  table.firstOf(rule));
		assertEquals(first,  table.firstOf(ruleName));
		assertEquals(first,  table.firstOf(ruleRef));
		
		first = new RealizedPromisseSet<MatchableProduction>(Identifier.instance(), Text.instance());
		 
		assertEquals(first,  table.firstOf(expression));
		assertEquals(first,  table.firstOf(terms));
		assertEquals(first,  table.firstOf(term));
		
		first = new RealizedPromisseSet<MatchableProduction>(Identifier.instance());
		 
		assertEquals(first,  table.firstOf(ruleRef));
	
		first = new RealizedPromisseSet<MatchableProduction>(Text.instance());
		 
		assertEquals(first,  table.firstOf(identifier));
		
		RealizedPromisseSet<MatchableProduction> follow = new RealizedPromisseSet<MatchableProduction>(EOFTerminal.instance());
		 
		assertEquals(follow,  table.followOf(rules));
	
		follow = new RealizedPromisseSet<MatchableProduction>( Terminal.of("\n"), EOFTerminal.instance());
		 
		assertEquals(follow,  table.followOf(rule));
		
		follow = new RealizedPromisseSet<MatchableProduction>( Terminal.of("="));
		
		assertEquals(follow,  table.followOf(ruleName));

		follow = new RealizedPromisseSet<MatchableProduction>( Terminal.of(";"));
		 
		assertEquals(follow,  table.followOf(expression));
		
		follow = new RealizedPromisseSet<MatchableProduction>( Terminal.of(";") , Terminal.of("|"));
		 
		assertEquals(follow,  table.followOf(terms));

		
		follow = new RealizedPromisseSet<MatchableProduction>( Terminal.of(";") , Terminal.of("|"), Terminal.of(","));
		 
		assertEquals(follow,  table.followOf(term));
		assertEquals(follow,  table.followOf(ruleRef));
		assertEquals(follow,  table.followOf(identifier));

	}
	
	@Test 
	public void testLookupTable() throws IOException {


		EBnfLanguage lang = new EBnfLanguage();
		
		LookupTable table = lang.getLookupTable();
		
	//	System.out.println(table);
		
		assertNotNull(table);
	}

	
}
