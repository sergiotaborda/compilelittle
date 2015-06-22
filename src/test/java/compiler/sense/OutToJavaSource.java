/**
 * 
 */
package compiler.sense;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import compiler.CompilerBackEnd;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;

/**
 * 
 */
public class OutToJavaSource implements CompilerBackEnd {

	PrintWriter writer;
	
	/**
	 * Constructor.
	 * @param out
	 * @throws IOException 
	 */
	public OutToJavaSource(File out) throws IOException {
		this.writer = new PrintWriter(new FileWriter(out));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void use(AstNode root) {
		JavaSourceWriterVisitor vv = new JavaSourceWriterVisitor(writer);
		TreeTransverser.tranverse(root, vv);
		writer.close();
	}

}
