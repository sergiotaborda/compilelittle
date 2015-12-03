/**
 * 
 */
package compiler.bnf;

import compiler.AstCompiler;


/**
 * 
 */
public final class BnfCompiler extends AstCompiler {

	/**
	 * Constructor.
	 * @param language
	 */
	public BnfCompiler() {
		super(new EBnfLanguage());
	}
}
