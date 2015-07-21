
/**
 * 
 */
package compiler.sense;

import compiler.typesystem.Type;


/**
 * 
 */
public class ExpressionNode extends SenseAstNode implements TypedNode {

	protected Type type;

	public Type getType() {
		return type;
	}


}
