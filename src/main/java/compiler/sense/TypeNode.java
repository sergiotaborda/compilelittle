/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class TypeNode extends AstNode {

	private boolean isVoid;
	private QualifiedName name;

	/**
	 * Constructor.
	 * @param b
	 */
	public TypeNode(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * Constructor.
	 * @param object
	 */
	public TypeNode(QualifiedName name) {
		this.name = name;
	}

}
