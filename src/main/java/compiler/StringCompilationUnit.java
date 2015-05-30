package compiler;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;

public class StringCompilationUnit  implements CompilationUnit{

	
	private String unit;

	public StringCompilationUnit (String unit){
		this.unit = unit;
	}
	
	@Override
	public Reader read() throws FileNotFoundException {
		return new StringReader(unit);
	}

}
