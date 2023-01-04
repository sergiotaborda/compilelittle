/**
 * 
 */
package compiler;

import java.util.stream.Stream;

/**
 * 
 */
public interface CompilationUnitSet extends Iterable<CompilationUnit> {

	
	public static CompilationUnitSet of (CompilationUnit unit) {
		var unitSet = new ListCompilationUnitSet();
		unitSet.add(unit);
		return unitSet;
	}
	
	Stream<CompilationUnit> stream(); 
}
