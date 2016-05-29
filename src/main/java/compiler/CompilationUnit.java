/**
 * 
 */
package compiler;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

/**
 * 
 */
public interface CompilationUnit {

	
	public Reader read() throws IOException;

	/**
	 * @return
	 */
	public String getName();
	
	public Path getOrigin();

}
