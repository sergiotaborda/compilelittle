package compiler.filesystem;

public interface SourceFileSystem {

	public SourceFile file(SourcePath path);

	public SourceFolder folder(SourcePath path);
}
