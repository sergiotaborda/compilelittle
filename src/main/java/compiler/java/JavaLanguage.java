/**
 * 
 */
package compiler.java;

import compiler.Language;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;

/**
 * 
 */
public class JavaLanguage extends Language{

	/**
	 * Constructor.
	 * @param grammar
	 */
	public JavaLanguage() {
		super(new JavaGrammar());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstNode transform(ParserTreeNode root) {
		return new AstNode();
	}

}
