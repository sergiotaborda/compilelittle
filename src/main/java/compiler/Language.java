/**
 * 
 */
package compiler;

import compiler.parser.BottomUpParser;
import compiler.parser.LALRAutomatonFactory;
import compiler.parser.LookupTable;
import compiler.parser.Parser;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;
import compiler.typesystem.TypesRepository;

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
	

	public abstract AstNode transform(ParserTreeNode root, TypesRepository repository);


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
