package compiler.math;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import compiler.Compiler;
import compiler.CompilerBackEnd;
import compiler.FileCompilationUnit;
import compiler.lexer.ListCompilationUnitSet;
import compiler.syntax.AstNode;

public class TestMathCompiler {

	@Test
	public void testCompilar() throws IOException {

		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/equation.math");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));


		final Compiler compiler = new Compiler(new MathLanguage());
		compiler.addBackEnd(new CompilerBackEnd(){

			@Override
			public void use(AstNode root) {
				// TODO Auto-generated method stub
				
			}});
		compiler.compile(unitSet);

	}
}
