/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.SenseType;


/**
 * 
 */
public class StringValue extends LiteralExpressionNode {

	private String value;
	
	public SenseType getType() {
		return SenseType.String;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
