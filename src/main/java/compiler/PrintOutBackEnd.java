/**
 * 
 */
package compiler;

import compiler.syntax.AstNode;
import compiler.syntax.XmlPrintOutAbstractTreeVisitor;
import compiler.trees.TreeTransverser;

/**
 * 
 */
public class PrintOutBackEnd implements CompilerBackEnd {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void use(AstNode root) {
		System.out.println();
		TreeTransverser.tranverse(root, new XmlPrintOutAbstractTreeVisitor());
		
	}

}
