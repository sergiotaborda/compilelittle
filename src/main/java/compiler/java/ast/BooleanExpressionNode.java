/**
 * 
 */
package compiler.java.ast;

import compiler.sense.typesystem.SenseType;

/**
 * 
 */
public class BooleanExpressionNode extends ExpressionNode {

	public SenseType getType() {
		return SenseType.Boolean;
	}

}
