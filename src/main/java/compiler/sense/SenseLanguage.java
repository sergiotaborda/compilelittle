/**
 * 
 */
package compiler.sense;

import compiler.Language;
import compiler.parser.LookupTable;
import compiler.parser.Parser;
import compiler.parser.nodes.ParserTreeNode;
import compiler.sense.SenseSemantic.AnalisisVisitor;
import compiler.sense.ast.UnitTypes;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;
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
		
		// Garanties Semantic is correct
		semantic.analise(t);
		
		// Transform literals to instances of objects
		TreeTransverser.tranverse(t,new LiteralsInstanciatorVisitor());
		
		return t;
	}

}
