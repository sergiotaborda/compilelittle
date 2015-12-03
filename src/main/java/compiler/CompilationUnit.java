/**
 * 
 */
package compiler;

import java.io.IOException;
import java.io.Reader;

/**
 * 
 */
public interface CompilationUnit {

	
	public Reader read() throws IOException;

	/**
	 * @return
	 */
	public String getName();
	
	public String getOrigin();

}
