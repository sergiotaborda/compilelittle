/**
 * 
 */
package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

/**
 * 
 */
public class FileCompilationUnit implements CompilationUnit {

	
	private File file;

	public FileCompilationUnit(File file){
		this.file = file;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws FileNotFoundException 
	 */
	@Override
	public Reader read() throws FileNotFoundException {
		return new FileReader(file);
	}

}
