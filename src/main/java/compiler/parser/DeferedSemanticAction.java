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
	
	public String toString(){
		return target.toString() + " read symbols = " + read.toString();
	}
	
	public void execute() {
		target.executeSemanticActions(left, read);
	}

	public ProductionStackItem getLeft() {
		return left;
	}

	public  List<Symbol>  getRead() {
		return read;
	}
	

}
