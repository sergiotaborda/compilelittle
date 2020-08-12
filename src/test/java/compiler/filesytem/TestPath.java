package compiler.filesytem;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.filesystem.SourcePath;

public class TestPath {

	@Test
	public void testRelativize() {
	
		var a = SourcePath.of("a","b","c");
		var b = SourcePath.of("a","b","c","d","file.h");
		
		assertEquals(SourcePath.of("d","file.h"), a.relativize(b));
	}

}
