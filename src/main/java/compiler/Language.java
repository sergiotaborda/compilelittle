/**
 * 
 */
package compiler;

import compiler.parser.BottomUpParser;
import compiler.parser.LALRAutomatonFactory;
import compiler.parser.LookupTable;
import compiler.parser.Parser;

/**
 * 
 */
public abstract class Language {

	
	private Grammar grammar;

	public Language(Grammar grammar) {
		this.grammar = grammar;
	}
	
	public Grammar getGrammar(){
		return grammar;
	}
	
	/**
	 * @return
	 */
	public LookupTable getLookupTable() {
		return new LALRAutomatonFactory().create().produceLookupTable(getGrammar());	
	}
	
	public Parser parser() {
		return new BottomUpParser(this);
	}
}
