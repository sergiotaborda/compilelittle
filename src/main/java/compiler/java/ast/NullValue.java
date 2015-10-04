/**
 * 
 */
package compiler.java.ast;

import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.TypeDefinition;



/**
 * 
 */
public class NullValue extends LiteralExpressionNode {

	public TypeDefinition getTypeDefinition() {
		return SenseTypeSystem.getInstance().getForName("None").get();
	}
}
