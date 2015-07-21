/**
 * 
 */
package compiler.sense;

import java.util.stream.Collectors;

import compiler.sense.typesystem.ConcreteMethodParameter;


/**
 * 
 */
public class ArgumentListNode extends SenseAstNode {

	
	public ConcreteMethodParameter[] asMethodParameters(){
		return this.getChildren().stream().map(v -> new ConcreteMethodParameter(((TypedNode)v).getType()))
		.collect(Collectors.toList())
		.toArray(new ConcreteMethodParameter[0]);
	}
}
