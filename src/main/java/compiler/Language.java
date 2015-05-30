/**
 * 
 */
package compiler;

import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;

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
	
	public abstract AstNode transform(ParserTreeNode root);
}
