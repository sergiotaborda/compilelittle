/**
 * 
 */
package compiler.math;

import java.util.Deque;
import java.util.LinkedList;

import compiler.parser.nodes.ParserTreeNode;
import compiler.parser.nodes.ParserTreeVisitor;
import compiler.syntax.AstNode;

/**
 * 
 */
public class AstBuilderVisitor implements ParserTreeVisitor {

	AstNode base = new AstNode();
	Deque<AstNode> stack = new LinkedList<>();
	
	public AstNode getRoot(){
		return base;
	}
	
	public AstBuilderVisitor(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startVisit() {
		stack.add(base);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endVisit() {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitBeforeChildren(ParserTreeNode node) {
		AstNode astNode = new AstNode();
		astNode.setProperty("parsed", node);
		stack.push(astNode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitAfterChildren(ParserTreeNode node) {
//		ParserTreeNode me = (ParserTreeNode)node;
//		
//		int size = node.getChildren().size();
//		if (size ==0){
//			
////			
////			ParserTreeNode parent = node.getParent();
////			ParserTreeNode grandParent = parent.getParent();
////			
////			if (parent != null && grandParent!= null){
////				parent.remove(me);
////				grandParent.remove(parent);
////				
////				if (!me.isEmpty()){
////					grandParent.add(me);
////				}
////				
////			}
//			
//			AstNode n = stack.pop();
//			stack.pop();
//			if (!me.isEmpty()){
//				stack.peek().add(n);
//			}
//		} else if (size == 1){
//			AstNode n = stack.pop();
//			stack.pop();
//			if (!me.isEmpty()){
//				if (stack.isEmpty()){
//					stack.add(n);
//				} else {
//					stack.peek().add(n);
//				}
//				
//			}
//		} else {
//			
//		}
		
	}

}
