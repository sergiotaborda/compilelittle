/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class BooleanValue extends LiteralExpressionNode {

	
	private boolean value;
	public Type getType() {
		return Type.Boolean;
	}
	public boolean isValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
}
