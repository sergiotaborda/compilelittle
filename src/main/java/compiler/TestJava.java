/**
 * 
 */
package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import compiler.java.JavaLanguage;
import compiler.lexer.ListCompilationUnitSet;

/**
 * 
 */
public class TestJava {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/compiler/Test1.java");
		
		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));
		

		try {
			
			Compiler compiler = new Compiler(new JavaLanguage());
			compiler.addBackEnd(new PrintOutBackEnd());
			
			compiler.compile(unitSet);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
