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
		VisitorNext next = visitor.visitBeforeChildren(node);
		
		if (next == VisitorNext.Children){
			LinkedList<T> list = new LinkedList<>(node.getChildren());
			
			while(!list.isEmpty()) {
				T child = list.removeFirst();
				// children can be replaces, so do not visit if is no longer a child
				if (node.getChildren().contains(child)){
					visit(child, visitor);
				}
			
			}
			
			visitor.visitAfterChildren(node);
		}

		
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
	
	public static <T extends Node<T>, R extends Node<R>> R transform(T root , Function<T,R> transformer){
		
		return transforms(root, transformer);

	}
	
	private static <T extends Node<T>, R extends Node<R>> R transforms(T root, Function<T, R> transformer) {
		R newRoot = transformer.apply(root);
		
		if (newRoot != null){
			if (newRoot.getChildren().size() != root.getChildren().size()){
				for(T n : root.getChildren()){
					R node = transforms(n, transformer);
					if (node != null){
						newRoot.add(node);
					}
				}
			}
			
			if (newRoot.getClass().getName().equals(root.getClass().getName()) 
					&& newRoot.getChildren().size() != root.getChildren().size()){
				throw new RuntimeException("Wrong transformation of " + root.getClass().getName());
			}
		}
		
		return newRoot;
	}
}
