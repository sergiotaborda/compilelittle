package compiler.filesystem;

public class SourceFileSystemException extends RuntimeException {


	private static final long serialVersionUID = 3546842772204353489L;

	
	public SourceFileSystemException(Exception original) {
		super(original);
	}
	
	public SourceFileSystemException(String message) {
		super(message);
	}
}
