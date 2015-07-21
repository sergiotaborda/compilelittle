/**
 * 
 */
package compiler.java;

import compiler.Language;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;
import compiler.typesystem.TypesRepository;

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
	public AstNode transform(ParserTreeNode root, TypesRepository repository) {
		return new AstNode();
	}

}
