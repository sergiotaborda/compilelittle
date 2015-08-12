
/**
 * 
 */
package compiler.sense.ast;

import compiler.typesystem.Type;


/**
 * 
 */
public class ExpressionNode extends SenseAstNode implements TypedNode {

	private Type type;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
