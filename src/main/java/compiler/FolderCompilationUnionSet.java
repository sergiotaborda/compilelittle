/**
 * 
 */
package compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 
 */
public class FolderCompilationUnionSet implements CompilationUnitSet {

	
	private Path folderpath;
	private Predicate<String> filter;
	
	public FolderCompilationUnionSet (Path folderpath, Predicate<String> filter){
		this.folderpath = folderpath;
		this.filter = filter;
	}
	
	public FolderCompilationUnionSet (File folderpath, Predicate<String> filter){
		this.folderpath = folderpath.toPath();
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
		return filesInDir(folderpath, filter).map(path -> (CompilationUnit) new FileCompilationUnit(path));
				//Stream.of(folderpath.listFiles()).filter(filter).map(f -> (CompilationUnit) new FileCompilationUnit(f.toPath()));
	}

	public static Stream<Path> filesInDir(Path dir, Predicate<String> filter) {
	    return listFiles(dir).filter(p -> filter.test(p.getFileName().toString()))
	            .flatMap(path -> path.toFile().isDirectory() 
	                    ? filesInDir(path, filter) 
	                    : Stream.of(path)
	            );
	}
	 
	private static Stream<Path> listFiles(Path dir) {
	    try {
	        return Files.list(dir);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
}
