/**
 * 
 */
package compiler;

import java.io.FileNotFoundException;
import java.io.Reader;

/**
 * 
 */
public interface CompilationUnit {

	
	public Reader read() throws FileNotFoundException;
}
