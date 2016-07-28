/**
 * 
 */
package compiler.math.ast;

import java.util.Deque;

/**
 * 
 */
public class MultiplyNode extends OperatorNode {


	public String toString(){
		return "*";
	}
	
	@Override
	public void operate(Deque<Value> stack) {
		
		Value a = (Value) stack.pop();
		Value b = (Value) stack.pop();
		
		stack.push(new Value(a.value * b.value));
	}
}
