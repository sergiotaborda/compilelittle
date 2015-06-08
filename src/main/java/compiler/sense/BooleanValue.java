/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class BooleanValue extends LiteralExpressionNode {

	public Type getType() {
		return Type.Boolean;
	}
}
