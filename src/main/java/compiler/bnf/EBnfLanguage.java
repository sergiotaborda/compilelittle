/**
 * 
 */
package compiler.bnf;

import compiler.Language;
/**
 * 
 */
public class EBnfLanguage extends Language {

	/**
	 * Constructor.
	 * @param grammar
	 */
	public EBnfLanguage() {
		super(new EBnfGrammar());
	}

}
