/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseType;


/**
 * 
 */
public class NullValue extends LiteralExpressionNode {

	public SenseType getType() {
		return SenseType.None;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteralValue() {
		return "null";
	}
}
