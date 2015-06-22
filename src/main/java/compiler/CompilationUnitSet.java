/**
 * 
 */
package compiler;

import java.util.stream.Stream;

/**
 * 
 */
public interface CompilationUnitSet extends Iterable<CompilationUnit> {

	
	Stream<CompilationUnit> stream(); 
}
