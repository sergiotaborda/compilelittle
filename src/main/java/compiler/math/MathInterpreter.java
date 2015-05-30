/**
 * 
 */
package compiler.math;

import java.util.Deque;
import java.util.LinkedList;

import compiler.CompilerBackEnd;
import compiler.math.ast.ExpressionNode;
import compiler.syntax.AstNode;

/**
 * 
 */
public class MathInterpreter implements CompilerBackEnd{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void use(AstNode root) {
		
		ExpressionNode exp = (ExpressionNode) root;
		
		Deque<ExpressionNode> vstack = new LinkedList<>();
		Deque<ExpressionNode> stack = new LinkedList<>();
		
		vstack.push(exp);
		
		while (!vstack.isEmpty()){
			ExpressionNode v = vstack.pop();
		
			for(AstNode a : v.getChildren()){
				vstack.push((ExpressionNode)a);
			}
			
			stack.push(v);
		}
		
		
		
	}

}
