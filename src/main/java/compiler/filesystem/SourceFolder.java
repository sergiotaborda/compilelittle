package compiler.filesystem;

import java.util.List;
import java.util.function.Predicate;

public interface SourceFolder extends SourceFileSystemNode {


	public boolean exists();

	public SourceFolder folder(String name);
	public SourceFolder folder(SourcePath path);


	public void ensureExists();

	public void delete();


	public default List<SourceFileSystemNode> children() {
		return children(it -> true);
	}

	public List<SourceFileSystemNode> children(Predicate<SourceFileSystemNode> predicate);

	public void walkTree(SourceWalker walker);
	
	public SourceFile file(String filename);
	public SourceFile file(SourcePath path);




	@Override
	public SourceFile asFile();


	@Override
	public SourceFolder asFolder();



	
	
}
