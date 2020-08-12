/**
 * 
 */
package compiler;

import java.io.IOException;
import java.io.Reader;

import compiler.filesystem.SourcePath;

/**
 * 
 */
public interface CompilationUnit {

	
	public Reader read() throws IOException;

	/**
	 * @return
	 */
	public String getName();
	
	public SourcePath getOrigin();
}
