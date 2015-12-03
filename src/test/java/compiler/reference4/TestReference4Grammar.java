/**
 * 
 */
package compiler.reference4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import compiler.AstCompiler;
import compiler.CompiledUnit;
import compiler.CompilerBackEnd;
import compiler.ListCompilationUnitSet;
import compiler.StringCompilationUnit;
import compiler.parser.LALRAutomatonFactory;
import compiler.parser.LookupTable;
import compiler.syntax.AstNode;

/**
 * 
 */
public class TestReference4Grammar {

	@Test
	public void testLookupTable() throws IOException {


		Reference4Grammar g = new Reference4Grammar();
		
		LookupTable table = new LALRAutomatonFactory().create().produceLookupTable(g);
	
		System.out.println(table);
		
		assertNotNull(table);
	}
	
	@Test 
	public void testCompilar() throws IOException {

		String text = "ccdd";
		
		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit(text));

		final AstCompiler compiler = new AstCompiler(new Reference4Language());

		final List<CompiledUnit> node = compiler.parse(unitSet).sendToList();
		
		assertEquals(1, node.size());
		
		assertNotNull(node.get(0));

	}

}
