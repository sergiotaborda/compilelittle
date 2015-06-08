/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


/**
 * 
 */
public class ClassInstanceCreation extends ExpressionNode{

	private TypeNode type;
	private ArgumentListNode argumentList;

	/**
	 * @param typeNode
	 */
	public void setType(TypeNode type) {
		this.type = type;
	}

	/**
	 * @param argumentListNode
	 */
	public void setArguments(ArgumentListNode argumentList) {
		this.argumentList = argumentList;
	}
	
	public Type getType() {
		return new Type(type.getName());
	}

}
