package compiler.reference1;

import compiler.Language;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;
import compiler.typesystem.TypesRepository;

public class ReferenceLanguage extends Language{

	public ReferenceLanguage() {
		super(new ReferenceGrammar());
	}

	@Override
	public AstNode transform(ParserTreeNode root, TypesRepository repository) {
		return TreeTransverser.copy(root, p -> {
			AstNode n = new AstNode();
			
			root.copyAttributes(a ->  n.setProperty(a.getKey(), a.getValue()));
		
			
			return n;
		});
	}

}
