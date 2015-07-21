/**
 * 
 */
package compiler.sense;

import compiler.Compiler;
import compiler.java.JavaTypeResolver;
/**
 * 
 */
public class SenseCompiler extends Compiler {

	/**
	 * Constructor.
	 * @param language
	 */
	public SenseCompiler() {
		super(new SenseLanguage());
		
		addTypeResolver(SenseTypeResolver.getInstance());
		addTypeResolver(new JavaToSenseTypeSystemAdapter(JavaTypeResolver.getInstance(),SenseTypeResolver.getInstance()));
	}

}
