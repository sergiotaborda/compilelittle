/**
 * 
 */
package compiler.sense;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import compiler.CompilerBackEnd;
import compiler.java.JavaSourceWriterVisitor;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;

/**
 * 
 */
public class OutToJavaSource implements CompilerBackEnd {

	File out;
	
	/**
	 * Constructor.
	 * @param out
	 * @throws IOException 
	 */
	public OutToJavaSource(File out) {
		this.out = out; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void use(AstNode root) {
		

		AstNode javaRoot = TreeTransverser.transform(root, new Sense2JavaTransformer());
		
		try(PrintWriter writer = new PrintWriter(new FileWriter(out))){
			TreeTransverser.tranverse(javaRoot, new JavaSourceWriterVisitor(writer));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
