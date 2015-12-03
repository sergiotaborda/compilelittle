/**
 * 
 */
package compiler;

import java.io.File;

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
		
		AstCompiler compiler = new AstCompiler(new MathLanguage2());

		compiler.parse(unitSet).peek(new PrintOutBackEnd()).sendTo(new MathInterpreter());
			

	}

}
