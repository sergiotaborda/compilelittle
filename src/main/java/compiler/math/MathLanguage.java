/**
 * 
 */
package compiler.math;

import compiler.Language;

/**
 * 
 */
public class MathLanguage extends Language{

	/**
	 * Constructor.
	 * @param grammar
	 */
	public MathLanguage() {
		super(new MathGrammar());
	}


}

