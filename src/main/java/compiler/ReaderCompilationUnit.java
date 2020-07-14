package compiler;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.nio.file.Path;

public class ReaderCompilationUnit implements CompilationUnit{

	
	private Reader unit;
	private Path origin;
	
	public ReaderCompilationUnit (Reader unit){
		this( unit, null);
	}
	
	public ReaderCompilationUnit (Reader unit,Path origin){
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
	public Path getOrigin() {
		return origin;
	}

}
