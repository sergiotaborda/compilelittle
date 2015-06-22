/**
 * 
 */
package compiler.parser.nodes;

import compiler.trees.VisitorNext;


/**
 * 
 */
public class XmlPrintOutParserTreeVisitor implements ParserTreeVisitor {

	int tabs = 0;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startVisit() {
		System.out.println("<parseTree>");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endVisit() {
		System.out.println("</parseTree>");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisitorNext visitBeforeChildren(ParserTreeNode node) {
		tabs++;
		printTabs();
		if (node.getChildren().isEmpty()){
			System.out.println("</" + node.toString() + ">");
		} else {
			System.out.println("<" + node.toString() + ">");
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
	public void visitAfterChildren(ParserTreeNode node) {
		
		if (!node.getChildren().isEmpty()){
			printTabs();
			System.out.println("</" + node.toString() + ">");
		}
		tabs--;
	}

}
