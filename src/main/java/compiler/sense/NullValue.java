/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class NullValue extends LiteralExpressionNode {

	public Type getType() {
		return Type.Any;
	}
}
