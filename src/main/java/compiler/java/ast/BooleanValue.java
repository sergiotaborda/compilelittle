/**
 * 
 */
package compiler.java.ast;

import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.TypeDefinition;



/**
 * 
 */
public class BooleanValue extends LiteralExpressionNode {

	
	private boolean value;
	public TypeDefinition getTypeDefinition() {
		return SenseTypeSystem.Boolean();
	}
	public boolean isValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
}
