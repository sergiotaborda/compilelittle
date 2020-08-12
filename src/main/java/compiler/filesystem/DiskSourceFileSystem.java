package compiler.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DiskSourceFileSystem implements SourceFileSystem {

	private static DiskSourceFileSystem me = new DiskSourceFileSystem();
	
	public static DiskSourceFileSystem instance() {
		return me;
	}
	
    SourceFileSystemNode convertFromFile(File file) {
		if (file.isFile()) {
			return new DiskSourceFile(this,file);
		}
		return new DiskSourceFolder(false,this, file);
	}
    
	private DiskSourceFileSystem() {
	
	}
	
	public SourceFolder folder(File folder) {
		return new DiskSourceFolder(true,this, folder);
	}
	
	public SourceFile file(File file) {
		return new DiskSourceFile(this, file);
	}
	
	public <N extends SourceFileSystemNode> List<File> convertToFiles(List<N> nodes) {
		return nodes.stream().map(f -> convertToFile(f)).collect(Collectors.toList());
	}

	public File convertToFile(SourceFileSystemNode node) {
		if (node instanceof DiskSourceFile) {
			return ((DiskSourceFile)node).file;
		} else if (node instanceof DiskSourceFolder) {
			return ((DiskSourceFolder)node).folder;
		}

		var f = diskFile(node.getPath());
		
		try {
			f.createNewFile();
			
			Files.copy(node.asFile().inputStream(), f.toPath());
			return f;
			
		} catch (IOException e) {
			throw handle(e);
		}
		
	
	}
	
	private File diskFile(SourcePath path) {
		
		if (path == null) {
			return new File(".").getAbsoluteFile();  
		}
		
		var deque = new LinkedList<String>();
		
		while(path != null) {
			
			deque.addFirst(path.getName());
			path = path.getParent();
		}
		
		var first = deque.removeFirst();
		File file = null;
		if(".".equals(first)) {
			file = new File(".").getAbsoluteFile();
		} else if("..".equals(first)) {
			file = new File(".").getAbsoluteFile().getParentFile();
		} else {
			file = new File(".").getAbsoluteFile();
			file = new File(file,first);
		}
	
		for(var p : deque) {
			file = new File(file,p);
		}
		
		return file;  
		
	}

	static RuntimeException handle(IOException e) {
		return new SourceFileSystemException(e);
	}
	

	@Override
	public SourceFile file(SourcePath path) {
		var folder = folder(path.getParent());
		
		return folder.file(path.getName()); 
	}
	

	@Override
	public SourceFolder folder(SourcePath path) {
		return new DiskSourceFolder(false, this, diskFile(path));  
		
	}

}
