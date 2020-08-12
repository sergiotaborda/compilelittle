package compiler;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;

import compiler.filesystem.SourcePath;

public class StringCompilationUnit  implements CompilationUnit{

	
	private String unit;
	private SourcePath origin;
	
	public StringCompilationUnit (String unit){
		this( unit, null);
	}
	
	public StringCompilationUnit (String unit,SourcePath origin){
		this.unit = unit;
		this.origin = origin;
	}
	
	@Override
	public Reader read() throws FileNotFoundException {
		return new StringReader(unit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return  unit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SourcePath getOrigin() {
		return origin;
	}

}
