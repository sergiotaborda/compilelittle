/**
 * 
 */
package compiler.sense;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import compiler.Compiler;
import compiler.FileCompilationUnit;
import compiler.bnf.EBnfLanguage;
import compiler.bnf.ToJavaBackEnd;
import compiler.lexer.ListCompilationUnitSet;

/**
 * 
 */
public class Builder {

	@Test
	public void test() throws IOException {
		
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/sense.bnf");
		File javaOut = new File(new File(".").getAbsoluteFile().getParentFile(), "src/main/java/compiler/sense/AbstractSenseGrammar.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));


		final Compiler compiler = new Compiler(new EBnfLanguage());
		compiler.addBackEnd(new ToJavaBackEnd(javaOut, "compiler.sense.AbstractSenseGrammar"));
		compiler.compile(unitSet);
	}

}
