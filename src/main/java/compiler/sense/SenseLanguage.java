/**
 * 
 */
package compiler.sense;

import compiler.Language;
import compiler.parser.LookupTable;
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
	
	public LookupTable getLookupTable() {
		return new SenseLookupTable((SenseGrammar)this.getGrammar());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstNode transform(ParserTreeNode root) {
		UnitTypes t = root.getProperty("node", UnitTypes.class).orElse(null);
		
		
		if (t == null){
			throw new RuntimeException("Compilation error");
		}
		SenseSemantic semantic = new SenseSemantic();
		
		semantic.analise(t);
		
		return t;
	}

}
