package compiler.bnf;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import compiler.Compiler;
import compiler.FileCompilationUnit;
import compiler.lexer.ListCompilationUnitSet;

/**
 * 
 */

/**
 * 
 */
public class TestCompiler {

	@Test
	public void testBnfCompilar() throws IOException {

		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/bigtest.bnf");
		File fileOut = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/test_out.bnf");
		File javaOut = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/AbstractJavaGrammar.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));


		final Compiler compiler = new Compiler(new EBnfLanguage());
		compiler.addBackEnd(new ToFileBackEnd(fileOut));
		compiler.addBackEnd(new ToJavaBackEnd(javaOut, "compiler.java.AbstractJavaGrammar"));
		compiler.compile(unitSet);

	}

}
