package compiler;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import compiler.filesystem.SourceFile;
import compiler.filesystem.SourceFileSystemNode;
import compiler.filesystem.SourceFolder;

public class SourceFolderCompilationUnitSet implements CompilationUnitSet {

	private final SourceFolder folder;
	private final Predicate<String> filter;

	public SourceFolderCompilationUnitSet (SourceFolder folder, Predicate<String> filter){
		this.folder = folder;
		this.filter = filter;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<CompilationUnit> iterator() {
		return stream().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<CompilationUnit> stream() {
		return filesInDir(folder, filter).map(path -> new SourceFileCompilationUnit(path));
	}

	public static Stream<SourceFile> filesInDir(SourceFolder dir, Predicate<String> filter) {
	    return listFiles(dir)
	            .flatMap(item -> item.isFolder()
	                    ? filesInDir((SourceFolder)item, filter) 
	                    : Stream.of((SourceFile)item)
	            ).filter(p -> filter.test(p.getName()));
	}
	 
	private static Stream<SourceFileSystemNode> listFiles(SourceFolder dir) {
	   return dir.children().stream();
	}
}
