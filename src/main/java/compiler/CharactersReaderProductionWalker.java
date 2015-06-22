/**
 * 
 */
package compiler;

import compiler.parser.NonTerminal;
import compiler.parser.Production;
import compiler.parser.ProductionVisitor;
import compiler.parser.Terminal;
import compiler.trees.VisitorNext;

/**
 * 
 */
public class CharactersReaderProductionWalker extends ProductionVisitor {

	private AbstractGrammar abstractGrammar;

	/**
	 * Constructor.
	 * @param abstractGrammar
	 */
	public CharactersReaderProductionWalker(AbstractGrammar abstractGrammar) {
		this.abstractGrammar = abstractGrammar;
	}

	
	protected VisitorNext visitBeforeNonTerminal(NonTerminal n, Production parent) {
		abstractGrammar.rules.add(n);
		return VisitorNext.Children;
	}
	
	
	protected VisitorNext visitTerminal(Terminal t, Production parent) {

		String text = t.getText();
		
		if (text != null){
			if (text.length() == 1 && !abstractGrammar.isAlphaNumeric(text.charAt(0))){
				abstractGrammar.stopCharacters.add(text.charAt(0));
				if (!abstractGrammar.isIgnore(text.charAt(0))){
					abstractGrammar.operators.add(text);
				}

			} else if (text.length() ==0){
				// no-op;
				return VisitorNext.Siblings;
			} 

			if (text.length() > 1 && !abstractGrammar.isAlphaNumeric(text.charAt(0)) && !abstractGrammar.isAlphaNumeric(text.charAt(1))){
				abstractGrammar.operators.add(text);
				for(int i =1; i <= text.length();i++){
					abstractGrammar.operators.add(text.substring(0,i));
					abstractGrammar.operators.add(text.substring(i-1,i));
				}
			} else {
				abstractGrammar.keywords.add(text);
			}
		}

		return VisitorNext.Siblings;

	}
}
