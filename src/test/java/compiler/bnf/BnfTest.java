/**
 * 
 */
package compiler.bnf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import compiler.Compiler;
import compiler.FileCompilationUnit;
import compiler.PrintOutBackEnd;
import compiler.lexer.ListCompilationUnitSet;

/**
 * 
 */
public class BnfTest {

	@Test
	public void test() {
	
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/test.bnf");
		
		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));
		
		try {
			
			final Compiler compiler = new Compiler(new EBnfLanguage());
			compiler.addBackEnd(new PrintOutBackEnd());
			compiler.compile(unitSet);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
