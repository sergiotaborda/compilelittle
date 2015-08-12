
/**
 * 
 */
package compiler.java.ast;

import compiler.typesystem.Type;


/**
 * 
 */
public class ExpressionNode extends JavaAstNode implements TypedNode {

	protected Type type;

	public Type getType() {
		return type;
	}


}
