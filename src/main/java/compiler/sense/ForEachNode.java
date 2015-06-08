/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class ForEachNode extends StatementNode {

	private VariableDeclarationNode variableDeclarationNode;
	private ExpressionNode container;
	private BlockNode blockNode;

	/**
	 * @param variableDeclarationNode
	 */
	public void setIterableVariable(
			VariableDeclarationNode variableDeclarationNode) {
		this.variableDeclarationNode = variableDeclarationNode;
	}

	/**
	 * @param expressionNode
	 */
	public void setContainer(ExpressionNode expressionNode) {
		this.container = expressionNode;
	}

	/**
	 * @param blockNode
	 */
	public void setBlock(BlockNode blockNode) {
		this.blockNode = blockNode; 
	}

}
