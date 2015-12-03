/**
 * 
 */
package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 */
public class FileCompilationUnit implements CompilationUnit {

	
	private Path path;

	public FileCompilationUnit(File file){
		this.path = file.toPath();
	}
	
	public FileCompilationUnit(Path path){
		this.path = path;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws FileNotFoundException 
	 */
	@Override
	public Reader read() throws IOException {
	    return Files.newBufferedReader(path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return path.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getOrigin() {
		return path.toString();
	}

}
