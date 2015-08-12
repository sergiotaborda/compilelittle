/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseType;


/**
 * 
 */
public class BooleanValue extends LiteralExpressionNode {

	
	private boolean value;
	public SenseType getType() {
		return SenseType.Boolean;
	}
	public boolean isValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteralValue() {
		return value ? "true" : "false";
	}
}
