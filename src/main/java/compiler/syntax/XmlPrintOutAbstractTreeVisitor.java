/**
 * 
 */
package compiler.syntax;

import compiler.trees.Node;
import compiler.trees.Visitor;
import compiler.trees.VisitorNext;

/**
 * 
 */
public class XmlPrintOutAbstractTreeVisitor implements Visitor<AstNode> {

	int tabs = 0;
	
	public XmlPrintOutAbstractTreeVisitor(){}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startVisit() {
		System.out.println("<abstractTree>");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endVisit() {
		System.out.println("</abstractTree>");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisitorNext visitBeforeChildren(AstNode node) {
		tabs++;
		printTabs();
		if (node !=null){
			if (node.getChildren().isEmpty()){
				System.out.println("</" + node.toString() + ">");
			} else {
				System.out.println("<" + node.toString() + ">");
			}
		}
		return VisitorNext.Children;
		
	}
	
	private void printTabs(){
		for(int i =0; i < tabs;i++){
			System.out.print(" ");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitAfterChildren(AstNode node) {
		
		if (!node.getChildren().isEmpty()){
			printTabs();
			System.out.println("</" + node.toString() + ">");
		}
		tabs--;
	}

}
