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

	
	default VisitorNext visitBeforeChildren(T node) {
		 return VisitorNext.Children;
	}
	
	void visitAfterChildren(T node);
}
