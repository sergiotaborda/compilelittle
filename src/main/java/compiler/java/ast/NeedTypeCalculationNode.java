/**
 * 
 */
package compiler.java.ast;

import compiler.typesystem.Type;

/**
 * 
 */
public class NeedTypeCalculationNode extends ExpressionNode {

	public void setType(Type type) {
		this.type = type;
	}
}
