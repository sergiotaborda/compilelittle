/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseType;


/**
 * 
 */
public class StringValue extends LiteralExpressionNode {

	private String value;
	
	public StringValue(){}
	
	public StringValue(String value){
		this.value = value;
	}
	
	public SenseType getType() {
		return SenseType.String;
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
