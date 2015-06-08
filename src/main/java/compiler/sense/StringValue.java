/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class StringValue extends LiteralExpressionNode {

	public Type getType() {
		return Type.String;
	}
}
