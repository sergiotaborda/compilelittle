/**
 * 
 */
package compiler.math.ast;

import java.util.Deque;

import compiler.syntax.AstNode;


/**
 * 
 */
public abstract class MathExpressionNode extends AstNode  {

	public abstract void operate(Deque<Value> values);

	
	
}
