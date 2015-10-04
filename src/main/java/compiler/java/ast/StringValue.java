/**
 * 
 */
package compiler.java.ast;

import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.TypeDefinition;


/**
 * 
 */
public class StringValue extends LiteralExpressionNode {

	private String value;
	
	public TypeDefinition getTypeDefinition() {
		return SenseTypeSystem.String();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
