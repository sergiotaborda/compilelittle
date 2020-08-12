package compiler.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DiskSourceFolder implements SourceFolder {

	final File folder;
	private DiskSourceFileSystem fileSystem;
	private boolean isRoot;

	DiskSourceFolder(boolean isRoot,DiskSourceFileSystem fileSystem, File folder){
		this.fileSystem = fileSystem;
		this.folder= folder;
		this.isRoot = isRoot;
	}

	@Override
	public boolean exists() {
		return folder.exists();
	}

	@Override
	public SourceFolder folder(String name) {
		return new DiskSourceFolder(false,fileSystem, new File(folder, name));
	}

	@Override
	public SourceFile file(String filename) {
		return new DiskSourceFile(fileSystem, new File(folder, filename));
	}

	@Override
	public SourceFolder folder(SourcePath path) {
		if(path == null) {
			return this;
		}
		return new DiskSourceFolder(false,fileSystem, new File(folder, path.join("/")));
	}
	
	@Override
	public SourceFile file(SourcePath path) {
		return new DiskSourceFile(fileSystem, new File(folder, path.join("/")));
	}
	
	public String toString() {
		return this.folder.toString();
	}
	
	@Override
	public void ensureExists() {
		folder.mkdirs();
	}

	@Override
	public void delete() {
		try {
			Files.walkFileTree(folder.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw DiskSourceFileSystem.handle(e);
		}
	}

	@Override
	public boolean isFolder() {
		return true;
	}

	@Override
	public boolean isFile() {
		return false;
	}

	@Override
	public SourceFile asFile() {
		throw new SourceFileSystemException(this.folder + " is not a file");
	}

	@Override
	public SourceFolder asFolder() {
		return this;
	}

	@Override
	public SourceFolder parentFolder() {
		if(isRoot || this.folder == null) {
			return null;
		}
		return new DiskSourceFolder(false,fileSystem, this.folder.getParentFile());
	}

	@Override
	public List<SourceFileSystemNode> children(Predicate<SourceFileSystemNode> predicate) {
		var files = folder.listFiles(f -> predicate.test(fileSystem.convertFromFile(f)));
		
		var result = new ArrayList<SourceFileSystemNode>(files.length);
		
		for(var f : files) {
			result.add(fileSystem.convertFromFile(f));
		}

		return result;
	}

	
	@Override
	public SourcePath getPath() {
		if(isRoot) {
			return new SourcePath(null, folder.getAbsolutePath());
		}
		
		if(this.parentFolder() == null) {
			return SourcePath.of(); 
		}
		
		return SourcePath.of(this.parentFolder().getPath(), folder.getName()); 
	}

	


	@Override
	public long lastModified() {
		return folder.lastModified();
	}

	@Override
	public String getName() {
		return folder.getName();
	}

	@Override
	public void walkTree(SourceWalker walker) {
		if(walker == null) {
			return;
		}
		try {
			Files.walkFileTree(this.folder.toPath(), new FileVisitor<Path>() {
	
				@Override
				public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes atts) throws IOException {
					return translateResult(walker.preVisitFolder(fileSystem.convertFromFile(path.toFile()).asFolder()));
				}
	
				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes mainAtts)
						throws IOException {
					return translateResult(walker.visitFile(fileSystem.convertFromFile(path.toFile()).asFile()));
				}
	
				@Override
				public FileVisitResult postVisitDirectory(Path path,
						IOException exc) throws IOException {
					return translateResult(walker.postVisitFolder(fileSystem.convertFromFile(path.toFile()).asFolder()));
				}
	
				@Override
				public FileVisitResult visitFileFailed(Path path, IOException exc) throws IOException {
					exc.printStackTrace();
	
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw DiskSourceFileSystem.handle(e);
		}

	}

	private static FileVisitResult translateResult(SourceWalkerResult result) {
		switch(result) {
		case TERMINATE:
			return FileVisitResult.TERMINATE;
		case SKIP_SIBLINGS:
			return FileVisitResult.SKIP_SIBLINGS;
		case SKIP_SUBTREE:
			return FileVisitResult.SKIP_SUBTREE;
		case CONTINUE:
		default:
			return FileVisitResult.CONTINUE;
		}
	}


}
