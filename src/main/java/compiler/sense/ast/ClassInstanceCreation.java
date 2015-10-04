/**
 * 
 */
package compiler.sense.ast;

import compiler.typesystem.TypeDefinition;

/**
 * 
 */
public class ClassInstanceCreation extends ExpressionNode{

	private TypeNode typeNode;
	private ArgumentListNode argumentList;

	public ClassInstanceCreation (){}
	
	public ClassInstanceCreation (TypeDefinition type, SenseAstNode ... args){
		TypeNode t = new TypeNode(type);
		setTypeNode(t);

		ArgumentListNode list = new ArgumentListNode();
		
		for(SenseAstNode s : args){
			list.add(s);
		}
		
		setArguments(list);
	}
	/**
	 * @param typeNode
	 */
	public void setTypeNode(TypeNode type) {
		this.typeNode = type;
		this.add(type);
	}
	
	public TypeNode getTypeNode(){
		return typeNode;
	}

	/**
	 * @param argumentListNode
	 */
	public void setArguments(ArgumentListNode argumentList) {
		this.argumentList = argumentList;
		this.add(argumentList);
	}
	
	/**
	 * Obtains {@link ArgumentListNode}.
	 * @return the argumentList
	 */
	public ArgumentListNode getArguments() {
		return argumentList;
	}

	/**
	 * Obtains {@link TypeNode}.
	 * @return the type
	 */
	public TypeDefinition getTypeDefinition() {
		return typeNode.getTypeDefinition();
	}


	

}
