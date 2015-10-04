/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.TypeDefinition;


/**
 * 
 */
public class ReturnNode extends StatementNode implements TypedNode {

	
	public ReturnNode(){
		
	}
	/**
	 * @param expressionNode
	 */
	public void setValue(ExpressionNode expressionNode) {
		this.add(expressionNode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getTypeDefinition() {
		return this.getChildren().isEmpty() ? SenseTypeSystem.Void() : ((ExpressionNode)this.getChildren().get(0)).getTypeDefinition();
	}
	
	

}
