/**
 * 
 */
package compiler.sense;

import compiler.Language;
import compiler.parser.BottomUpParser;
import compiler.parser.LookupTable;
import compiler.parser.Parser;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;
import compiler.typesystem.TypesRepository;

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
	
	public Parser parser() {
		return new SenseParser(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstNode transform(ParserTreeNode root,TypesRepository repository) {
		UnitTypes t = root.getProperty("node", UnitTypes.class).orElse(null);
		
		
		if (t == null){
			throw new RuntimeException("Compilation error");
		}
		SenseSemantic semantic = new SenseSemantic(repository);
		
		semantic.analise(t);
		
		return t;
	}

}
