package compiler.filesystem;

public class RootSourcePath extends SourcePath {

	RootSourcePath(String completeName) {
		super(null, completeName);
	}
	
	public SourcePath getParent() {
//		root has no parents
		return null; 
	}

}
