/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;


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
	public Type getType() {
		return ((ExpressionNode)this.getChildren().get(0)).getType();
	}
	
	

}
