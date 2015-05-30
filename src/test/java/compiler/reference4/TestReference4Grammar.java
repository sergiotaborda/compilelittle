/**
 * 
 */
package compiler.reference4;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import compiler.Compiler;
import compiler.CompilerBackEnd;
import compiler.StringCompilationUnit;
import compiler.lexer.ListCompilationUnitSet;
import compiler.parser.BottomUpParser;
import compiler.parser.LookupTable;
import compiler.reference1.ReferenceGrammar;
import compiler.reference1.ReferenceLanguage;
import compiler.syntax.AstNode;

/**
 * 
 */
public class TestReference4Grammar {

	@Test
	public void testLookupTable() throws IOException {


		Reference4Grammar g = new Reference4Grammar();
		
		LookupTable table = ((BottomUpParser)g.parser()).getLookupTable();
		
		System.out.println(table);
		
		assertNotNull(table);
	}
	
	@Test 
	public void testCompilar() throws IOException {

		String text = "ccdd";
		
		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit(text));

		final List<AstNode> node = new ArrayList<>(1);
		
		final Compiler compiler = new Compiler(new Reference4Language());
		compiler.addBackEnd(new CompilerBackEnd(){

			@Override
			public void use(AstNode root) {
				node.add(root);
			}});
		
		compiler.compile(unitSet);
		
		assertEquals(1, node.size());
		
		AstNode n = node.get(0);
		
		assertNotNull(n);

	}

}