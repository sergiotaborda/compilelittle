/**
 * 
 */
package compiler.sense;

import compiler.Language;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;

/**
 * 
 */
public class SenseLanguage extends Language{

	/**
	 * Constructor.
	 * @param grammar
	 */
	public SenseLanguage() {
		super(new SenseGrammar());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstNode transform(ParserTreeNode root) {
		UnitTypes t = root.getProperty("node", UnitTypes.class).orElse(null);
		
		SenseSemantic semantic = new SenseSemantic();
		
		semantic.analise(t);
		
		return t;
	}

}
