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

/**
 * 
 */
public class PrintToJava implements CompilerBackEnd {

	PrintWriter writer;
	
	/**
	 * Constructor.
	 * @param out
	 * @throws IOException 
	 */
	public PrintToJava(File out) throws IOException {
		this.writer = new PrintWriter(new FileWriter(out));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void use(AstNode root) {

	}

}
