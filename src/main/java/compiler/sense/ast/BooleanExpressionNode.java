/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseType;

/**
 * 
 */
public class BooleanExpressionNode extends ExpressionNode {

	public SenseType getType() {
		return SenseType.Boolean;
	}

}
