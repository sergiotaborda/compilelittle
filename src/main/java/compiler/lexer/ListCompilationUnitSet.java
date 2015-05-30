/**
 * 
 */
package compiler.lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import compiler.CompilationUnit;
import compiler.CompilationUnitSet;

/**
 * 
 */
public class ListCompilationUnitSet implements CompilationUnitSet {

	List<CompilationUnit> list = new ArrayList<>();
	
	public ListCompilationUnitSet(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<CompilationUnit> iterator() {
		return list.iterator();
	}
	
	public void add(CompilationUnit unit){
		list.add(unit);
	}

}
