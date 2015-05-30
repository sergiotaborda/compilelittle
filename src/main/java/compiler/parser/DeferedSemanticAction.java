/**
 * 
 */
package compiler.parser;

import java.util.List;

/**
 * 
 */
class DeferedSemanticAction {

	private ProductionItem target;
	private ProductionStackItem left;
	private List<Symbol> read;

	/**
	 * Constructor.
	 * @param target
	 * @param left
	 * @param read
	 */
	public DeferedSemanticAction(ProductionItem target, ProductionStackItem left, List<Symbol> read) {
		this.target=  target;
		this.left = left;
		this.read = read;
	}
	
	public void execute() {
		target.executeSemanticActions(left, read);
	}
	

}
