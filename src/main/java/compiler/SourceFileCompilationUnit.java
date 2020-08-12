package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import compiler.filesystem.SourceFile;
import compiler.filesystem.SourcePath;

public class SourceFileCompilationUnit implements CompilationUnit {

	private SourceFile file;

	public SourceFileCompilationUnit(SourceFile file){
		this.file = file;
	}

	/**
	 * {@inheritDoc}
	 * @throws FileNotFoundException 
	 */
	@Override
	public Reader read() throws IOException {
	    return file.reader();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return file.toString();
	}


	public String toString() {
		return file.toString();
	}

	@Override
	public SourcePath getOrigin() {
		return file.getPath();
	}
}
