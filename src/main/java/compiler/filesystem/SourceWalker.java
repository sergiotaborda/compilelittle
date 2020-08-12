package compiler.filesystem;

public interface SourceWalker {

	public default SourceWalkerResult preVisitFolder(SourceFolder folder) {
		return SourceWalkerResult.CONTINUE;
	}

	public default SourceWalkerResult visitFile(SourceFile file){
		return SourceWalkerResult.CONTINUE;
	}

	public default SourceWalkerResult postVisitFolder(SourceFolder folder){
		return SourceWalkerResult.CONTINUE;
	}
}
