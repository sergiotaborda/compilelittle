package compiler.filesystem;

public interface SourceFileSystemNode {

	public SourcePath getPath();
	
	public boolean isFolder();

	public boolean isFile();

	public SourceFile asFile();
	
	public SourceFolder asFolder();
	
	public SourceFolder parentFolder();

	public long lastModified();

	public String getName();
}
