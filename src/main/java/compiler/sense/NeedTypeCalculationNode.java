/**
 * 
 */
package compiler.sense;

import compiler.typesystem.Type;

/**
 * 
 */
public class NeedTypeCalculationNode extends ExpressionNode {

	public void setType(Type type) {
		this.type = type;
	}
}
