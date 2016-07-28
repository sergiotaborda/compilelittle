/**
 * 
 */
package compiler.math;

import java.util.Deque;
import java.util.LinkedList;

import compiler.CompiledUnit;
import compiler.CompilerBackEnd;
import compiler.math.ast.MathExpressionNode;
import compiler.math.ast.Value;
import compiler.syntax.AstNode;

/**
 * 
 */
public class MathInterpreter implements CompilerBackEnd{

	private long value;
	public MathInterpreter (){}
	
	
	public long getValue(){
		return this.value;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void use(CompiledUnit unit) {

		MathExpressionNode exp = (MathExpressionNode) unit.getAstRootNode();

		Deque<MathExpressionNode> vstack = new LinkedList<>();
		Deque<MathExpressionNode> stack = new LinkedList<>();

		vstack.push(exp);

		// invert notation
		while (!vstack.isEmpty()){
			MathExpressionNode v = vstack.pop();

			if (v.getChildren().isEmpty()){
				stack.add(v);
			} else {
				
				vstack.push((MathExpressionNode) v.getChildren().get(1));
				vstack.push((MathExpressionNode) v.getChildren().get(0));
				vstack.push((MathExpressionNode) v.getChildren().get(2));
				
			}
		}

		Deque<Value> values = new LinkedList<>();

		while (!stack.isEmpty()){
			MathExpressionNode s = stack.pop();
			s.operate(values);
		}
		Value value = values.pop();
		
		this.value = value.getValue();
	}

}
