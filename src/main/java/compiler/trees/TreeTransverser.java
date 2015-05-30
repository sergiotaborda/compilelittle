/**
 * 
 */
package compiler.trees;

import java.util.LinkedList;
import java.util.function.Function;


/**
 * 
 */
public class TreeTransverser {

	
	public static <T extends Node<T>> void tranverse(T node , Visitor<T> visitor){
		
		if (node == null){
			return;
		}
		
		visitor.startVisit();
		
		visit(node, visitor);

		visitor.endVisit();
		
	}

	private static <T extends Node<T>> void visit(T node, Visitor<T> visitor) {
		visitor.visitBeforeChildren(node);
		
		LinkedList<T> list = new LinkedList<>(node.getChildren());
		
		while(!list.isEmpty()) {
			visit(list.removeFirst(), visitor);
		}
		
		visitor.visitAfterChildren(node);
	}
	
	public static <T extends Node<T>, R extends Node<R>> R copy(T root , Function<T,R> duplicator){
		
		return duplicate(root, duplicator);

	}

	private static <T extends Node<T>, R extends Node<R>> R duplicate(T root, Function<T, R> duplicator) {
		R newRoot = duplicator.apply(root);
		
		for(T n : root.getChildren()){
			newRoot.add(duplicate(n, duplicator));
		}
		return newRoot;
	}
	
	
}
