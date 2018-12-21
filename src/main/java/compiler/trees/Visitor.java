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
	default void startVisit() {}

	/**
	 * 
	 */
	default void endVisit(){}

	
	VisitorNext visitBeforeChildren(T node);
	
	void visitAfterChildren(T node);
}
