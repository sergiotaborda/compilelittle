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
		this.add(type);
	}

	/**
	 * @param argumentListNode
	 */
	public void setArguments(ArgumentListNode argumentList) {
		this.argumentList = argumentList;
		this.add(argumentList);
	}

	/**
	 * Obtains {@link TypeNode}.
	 * @return the type
	 */
	public Type getType() {
		return type.getType();
	}

	/**
	 * Obtains {@link ArgumentListNode}.
	 * @return the argumentList
	 */
	public ArgumentListNode getArgumentList() {
		return argumentList;
	}
	

}
