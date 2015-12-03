package compiler;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;

public class StringCompilationUnit  implements CompilationUnit{

	
	private String unit;
	private String origin;
	
	public StringCompilationUnit (String unit){
		this( unit, "");
	}
	
	public StringCompilationUnit (String unit,String origin){
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
	public String getOrigin() {
		return origin;
	}

}
