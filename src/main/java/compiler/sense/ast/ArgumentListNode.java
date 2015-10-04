/**
 * 
 */
package compiler.sense.ast;

import java.util.stream.Collectors;

import compiler.syntax.AstNode;
import compiler.typesystem.MethodParameter;


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
	public MethodParameter[] asMethodParameters(){
		return this.getChildren().stream().map(v -> new MethodParameter(((TypedNode)v).getTypeDefinition()))
		.collect(Collectors.toList())
		.toArray(new MethodParameter[this.getChildren().size()]);
	}
}
