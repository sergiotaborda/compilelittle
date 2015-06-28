/**
 * 
 */
package compiler.bnf;


/**
 * 
 */
public final class BnfCompiler extends compiler.Compiler{

	/**
	 * Constructor.
	 * @param language
	 */
	public BnfCompiler() {
		super(new EBnfLanguage());
	}

}
