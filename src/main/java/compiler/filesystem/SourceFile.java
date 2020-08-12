package compiler.filesystem;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public interface SourceFile extends SourceFileSystemNode {

	public InputStream inputStream();
	
	public OutputStream outputStream();

	@Override
	public boolean isFolder() ;

	
	public String getName();


	public Reader reader();

	public Writer writer();
	
	
	public boolean exists();

	
	public void ensureExists();


	public long lastModified();


	public void moveTo(SourceFile target);

	



}
