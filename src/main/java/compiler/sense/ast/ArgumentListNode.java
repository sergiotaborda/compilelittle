/**
 * 
 */
package compiler.sense.ast;

import java.util.stream.Collectors;

import compiler.sense.typesystem.ConcreteMethodParameter;
import compiler.syntax.AstNode;


/**
 * 
 */
public class ArgumentListNode extends SenseAstNode {

	public ArgumentListNode (){}
	
	public ArgumentListNode (AstNode... params){
		for(AstNode n : params){
			this.add(n);
		}
	}
	public ConcreteMethodParameter[] asMethodParameters(){
		return this.getChildren().stream().map(v -> new ConcreteMethodParameter(((TypedNode)v).getType()))
		.collect(Collectors.toList())
		.toArray(new ConcreteMethodParameter[0]);
	}
}
