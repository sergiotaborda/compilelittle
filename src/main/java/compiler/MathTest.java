/**
 * 
 */
package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import compiler.lexer.ListCompilationUnitSet;
import compiler.math.MathInterpreter;
import compiler.math.MathLanguage2;

/**
 * 
 */
public class MathTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/compiler/teste.math");
		
		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));
		
	
		try {
			
			Compiler compiler = new Compiler(new MathLanguage2());
			compiler.addBackEnd(new PrintOutBackEnd());
			compiler.addBackEnd(new MathInterpreter());
			compiler.compile(unitSet);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
