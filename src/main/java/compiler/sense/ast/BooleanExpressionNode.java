/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.TypeDefinition;

/**
 * 
 */
public class BooleanExpressionNode extends ExpressionNode {

	public TypeDefinition getTypeDefinition() {
		return SenseTypeSystem.Boolean();
	}

}
