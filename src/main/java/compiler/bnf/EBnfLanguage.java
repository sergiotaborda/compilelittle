/**
 * 
 */
package compiler.bnf;

import compiler.Language;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;
import compiler.typesystem.TypesRepository;
/**
 * 
 */
public class EBnfLanguage extends Language {

	/**
	 * Constructor.
	 * @param grammar
	 */
	public EBnfLanguage() {
		super(new EBnfGrammar());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstNode transform(ParserTreeNode root, TypesRepository repository) {


	//	TreeTransverser.tranverse(root, new XmlPrintOutParserTreeVisitor());
		
		AstNode ast = toAstNode(root);

	//	TreeTransverser.tranverse(ast, new XmlPrintOutAbstractTreeVisitor());

		return ast;

	}

	private AstNode toAstNode(ParserTreeNode root){

		return root.getProperty("node", AstNode.class).get();
		
	}

}
