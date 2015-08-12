/**
 * 
 */
package compiler.java.ast;

import compiler.sense.typesystem.SenseType;


/**
 * 
 */
public class NullValue extends LiteralExpressionNode {

	public SenseType getType() {
		return SenseType.None;
	}
}
