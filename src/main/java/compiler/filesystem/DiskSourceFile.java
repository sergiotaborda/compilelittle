package compiler.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DiskSourceFile implements SourceFile {

	final File file;
	private DiskSourceFileSystem fileSystem;


	DiskSourceFile(DiskSourceFileSystem fileSystem , File file) {
		this.fileSystem = fileSystem;
		this.file=file;
	}

	@Override
	public InputStream inputStream() {
		try {
			return new FileInputStream(file);
		} catch (IOException e) {
			throw DiskSourceFileSystem.handle(e);
		}
	}

	@Override
	public OutputStream outputStream() {
		try {
			return new FileOutputStream(file);
		} catch (IOException e) {
			throw DiskSourceFileSystem.handle(e);
		}
	}

	@Override
	public boolean isFolder() {
		return false;
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public Reader reader() {
		try {
			return Files.newBufferedReader(file.toPath());
		} catch (IOException e) {
			throw DiskSourceFileSystem.handle(e);
		}
	}

	@Override
	public Writer writer() {
		try {
			return Files.newBufferedWriter(file.toPath());
		} catch (IOException e) {
			throw DiskSourceFileSystem.handle(e);
		}
	}

	@Override
	public boolean isFile() {
		return true;
	}

	@Override
	public SourceFile asFile() {
		return this;
	}

	@Override
	public SourceFolder asFolder() {
		throw new SourceFileSystemException(this.file + " is not a folder");
	}

	@Override
	public boolean exists() {
		return file.exists();
	}

	@Override
	public void ensureExists() {
		try {
			file.createNewFile();
		} catch (IOException e) {
			throw DiskSourceFileSystem.handle(e);
		}
	}

	public String toString() {
		return this.file.toString();
	}
	
	@Override
	public SourceFolder parentFolder() {
		return new DiskSourceFolder(false,this.fileSystem,this.file.getParentFile());
	}

	
	@Override
	public SourcePath getPath() {
		return SourcePath.of(this.parentFolder().getPath(),this.file.getName());
	}

	@Override
	public long lastModified() {
		return file.lastModified();
	}
	

	@Override
	public void moveTo(SourceFile target) {
		try {
			if(target instanceof DiskSourceFile) {
				
				Files.move(this.file.toPath(), ((DiskSourceFile)target).file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			} else {
				this.inputStream().transferTo(target.outputStream());
				
				this.file.delete();
			}
		} catch (IOException e) {
			throw DiskSourceFileSystem.handle(e);
		}
	}

}
