/**
 * 
 */
package compiler.reference4;

import compiler.Language;

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
	
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public AstNode transform(ParserTreeNode root,TypeResolver resolver) {
//		return TreeTransverser.copy(root, p -> {
//			AstNode n = new AstNode();
//			
//			root.copyAttributes(a ->  n.setProperty(a.getKey(), a.getValue()));
//		
//			
//			return n;
//		});
//	}

}
