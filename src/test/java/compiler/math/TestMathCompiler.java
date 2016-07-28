package compiler.math;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import compiler.AstCompiler;
import compiler.ListCompilationUnitSet;
import compiler.PrintOutBackEnd;
import compiler.StringCompilationUnit;

public class TestMathCompiler {

	@Test
	public void testCompilar() throws IOException {


		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit("2+3*4"));

		final AstCompiler compiler = new AstCompiler(new MathLanguage());
		compiler.parse(unitSet).sendToList();

	}

	@Test
	public void testMathLanguage(){


		AstCompiler compiler = new AstCompiler(new MathLanguageWithExplicitPreference());

		MathInterpreter mp = new MathInterpreter();

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit("7 + 2 * 5"));

		compiler.parse(unitSet).peek(new PrintOutBackEnd()).sendTo(mp);

		assertEquals(17L, mp.getValue());

		unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit("7 + 2 * 5 + 4"));

		compiler.parse(unitSet).peek(new PrintOutBackEnd()).sendTo(mp);

		assertEquals(21L, mp.getValue());

		unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit("7 + 2 * 5 * 4"));

		compiler.parse(unitSet).peek(new PrintOutBackEnd()).sendTo(mp);

		assertEquals(47L, mp.getValue());

		unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit("20 + 1 - 5 + 4"));

		compiler.parse(unitSet).peek(new PrintOutBackEnd()).sendTo(mp);

		assertEquals(20L, mp.getValue());
		
		unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit("20 - 1 - 5 - 4"));

		compiler.parse(unitSet).peek(new PrintOutBackEnd()).sendTo(mp);

		assertEquals(10L, mp.getValue());
		
		unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit("20 - 1 - 5 + 4"));

		compiler.parse(unitSet).peek(new PrintOutBackEnd()).sendTo(mp);

		assertEquals(18L, mp.getValue());
		
		unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit("(20 - 1) - (5 + 4)"));

		compiler.parse(unitSet).peek(new PrintOutBackEnd()).sendTo(mp);

		assertEquals(10L, mp.getValue());
	}
}
