package compiler.bnf;
import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import compiler.AstCompiler;
import compiler.ListCompilationUnitSet;
import compiler.SourceFileCompilationUnit;
import compiler.filesystem.DiskSourceFileSystem;
import compiler.filesystem.SourcePath;

/**
 * 
 */

/**
 * 
 */
public class TestBnf {

	@Test @Ignore
	public void test() throws IOException {

		var fileSystem = DiskSourceFileSystem.instance().folder(new File(".").getAbsoluteFile().getParentFile());
		
		
		var file =  fileSystem.file(SourcePath.of("src","test","resources","test.bnf")); 
		var fileOut = fileSystem.file(SourcePath.of("src","test","resources","test_out.bnf"));  

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new SourceFileCompilationUnit(file));


		final AstCompiler compiler = new AstCompiler(new EBnfLanguage());
		compiler.parse(unitSet).sendTo(new ToFileBackEnd(fileOut));;

	}

}
