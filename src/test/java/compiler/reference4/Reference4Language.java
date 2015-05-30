/**
 * 
 */
package compiler.reference4;

import compiler.Language;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;

/**
 * 
 */
public class Reference4Language extends Language {

	/**
	 * Constructor.
	 * @param grammar
	 */
	public Reference4Language() {
		super(new Reference4Grammar());
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstNode transform(ParserTreeNode root) {
		return TreeTransverser.copy(root, p -> {
			AstNode n = new AstNode();
			
			root.copyAttributes(a ->  n.setProperty(a.getKey(), a.getValue()));
		
			
			return n;
		});
	}

}
