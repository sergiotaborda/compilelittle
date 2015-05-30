/**
 * 
 */
package compiler.trees;

/**
 * 
 */
public interface Visitor<T extends Node<T>> {

	/**
	 * 
	 */
	void startVisit();

	/**
	 * 
	 */
	void endVisit();

	
	void visitBeforeChildren(Node<T> node);
	
	void visitAfterChildren(Node<T> node);
}
