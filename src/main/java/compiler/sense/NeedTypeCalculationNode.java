/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;

/**
 * 
 */
public class NeedTypeCalculationNode extends ExpressionNode {

	public void setType(Type type) {
		this.type = type;
	}
}
