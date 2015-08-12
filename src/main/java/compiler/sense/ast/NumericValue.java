/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseType;


/**
 * 
 */
public class NumericValue extends LiteralExpressionNode {

	private Number number;

	public NumericValue (){};
	
	/**
	 * @param n
	 */
	public void setValue(Number n, SenseType type) {
		this.number = n;
		this.setType(type);
	}
	
	public String toString(){
		return number.toString();
	}

	/**
	 * @return
	 */
	public Number getValue() {
		return number;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteralValue() {
		return number.toString();
	}

}
