package compiler;

import java.io.FileNotFoundException;
import java.io.Reader;

import compiler.filesystem.SourcePath;

public class ReaderCompilationUnit implements CompilationUnit{

	
	private Reader unit;
	private SourcePath origin;
	
	public ReaderCompilationUnit (Reader unit){
		this( unit, null);
	}
	
	public ReaderCompilationUnit (Reader unit,SourcePath origin){
		this.unit = unit;
		this.origin = origin;
	}
	
	@Override
	public Reader read() throws FileNotFoundException {
		return unit;
	}

	/**
	 */
	@Override
	public String getName() {
		return  unit.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SourcePath getOrigin() {
		return origin;
	}

}
