package compiler.bnf;
import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import compiler.AstCompiler;
import compiler.FileCompilationUnit;
import compiler.ListCompilationUnitSet;

/**
 * 
 */

/**
 * 
 */
public class TestBnf {

	@Test @Ignore
	public void test() throws IOException {

		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/test.bnf");
		File fileOut = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/test_out.bnf");
		//File javaOut = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/AbstractJavaGrammar.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));


		final AstCompiler compiler = new AstCompiler(new EBnfLanguage());
		compiler.parse(unitSet).sendTo(new ToFileBackEnd(fileOut));;

	}

}
