/**
 * 
 */
package compiler.math;

import compiler.Language;

/**
 * 
 */
public class MathLanguageWithExplicitPreference extends Language{

	/**
	 * Constructor.
	 * @param grammar
	 */
	public MathLanguageWithExplicitPreference() {
		super(new MathGrammarWithExplicitPreference());
	}




}

