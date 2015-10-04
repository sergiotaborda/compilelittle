/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.TypeDefinition;


/**
 * 
 */
public class StringValue extends LiteralExpressionNode {

	private String value;
	
	public StringValue(){}
	
	public StringValue(String value){
		this.value = value;
	}
	
	public TypeDefinition getTypeDefinition() {
		return SenseTypeSystem.String();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteralValue() {
		return value;
	}


}
