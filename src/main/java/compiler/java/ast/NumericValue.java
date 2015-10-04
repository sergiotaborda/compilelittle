/**
 * 
 */
package compiler.java.ast;

import compiler.sense.typesystem.SenseTypeDefinition;



/**
 * 
 */
public class NumericValue extends LiteralExpressionNode {

	private Number number;
	
	public NumericValue(){}
	
	public NumericValue (Number n){
		this.number = n;
	}

	/**
	 * @param n
	 */
	public void setValue(Number n, SenseTypeDefinition type) {
		this.number = n;
		this.type = type;
	}
	
	public String toString(){
		return number.toString();
	}

}
