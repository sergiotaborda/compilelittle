package compiler.math;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import compiler.AstCompiler;
import compiler.FileCompilationUnit;
import compiler.ListCompilationUnitSet;

public class TestMathCompiler {

	@Test
	public void testCompilar() throws IOException {

		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/equation.math");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));


		final AstCompiler compiler = new AstCompiler(new MathLanguage());
		compiler.parse(unitSet).sendToList();

	}
}
