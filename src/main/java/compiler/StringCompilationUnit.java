package compiler;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;

public class StringCompilationUnit  implements CompilationUnit{

	
	private String unit;
	private Path origin;
	
	public StringCompilationUnit (String unit){
		this( unit, null);
	}
	
	public StringCompilationUnit (String unit,Path origin){
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
	public Path getOrigin() {
		return origin;
	}

}
