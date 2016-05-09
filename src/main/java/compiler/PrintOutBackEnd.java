/**
 * 
 */
package compiler;

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
	public void use(CompiledUnit unit) {
		System.out.println();
		TreeTransverser.transverse(unit.getAstRootNode(), new XmlPrintOutAbstractTreeVisitor());
		
	}

}
