/**
 * 
 */
package compiler.sense;

import compiler.Compiler;
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
	}

}
