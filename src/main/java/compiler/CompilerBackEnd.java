/**
 * 
 */
package compiler;

import compiler.syntax.AstNode;

/**
 * 
 */
public interface CompilerBackEnd {

	/**
	 * @param transform
	 */
	void use(AstNode root);

}
