/**
 * 
 */
package compiler.math;

import java.util.Deque;
import java.util.LinkedList;

import compiler.CompiledUnit;
import compiler.CompilerBackEnd;
import compiler.math.ast.MathExpressionNode;
import compiler.syntax.AstNode;

/**
 * 
 */
public class MathInterpreter implements CompilerBackEnd{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void use(CompiledUnit unit) {
		
		MathExpressionNode exp = (MathExpressionNode) unit.getAstRootNode();
		
		Deque<MathExpressionNode> vstack = new LinkedList<>();
		Deque<MathExpressionNode> stack = new LinkedList<>();
		
		vstack.push(exp);
		
		while (!vstack.isEmpty()){
			MathExpressionNode v = vstack.pop();
		
			for(AstNode a : v.getChildren()){
				vstack.push((MathExpressionNode)a);
			}
			
			stack.push(v);
		}
		
		
		
	}

}
