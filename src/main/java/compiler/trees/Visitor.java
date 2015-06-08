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

	
	void visitBeforeChildren(T node);
	
	void visitAfterChildren(T node);
}
