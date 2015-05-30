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
public class TestBnf {

	@Test
	public void test() throws IOException {

		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/test.bnf");
		File fileOut = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/test_out.bnf");
		//File javaOut = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/AbstractJavaGrammar.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));


		final Compiler compiler = new Compiler(new EBnfLanguage());
		compiler.addBackEnd(new ToFileBackEnd(fileOut));
		//compiler.addBackEnd(new ToJavaBackEnd(javaOut));
		compiler.compile(unitSet);

	}

}
