/**
 * 
 */
package compiler.parser.nodes;

import java.util.Map.Entry;
import java.util.function.Consumer;

import compiler.parser.Production;

/**
 * 
 */
public abstract class AbsractProductionBasedParserTreeNode extends ParserTreeNode {


	private Production production;
	
	public abstract AbsractProductionBasedParserTreeNode duplicate();


	@Override
	public void copyAttributes(Consumer<Entry<String, Object>> consumer) {

	}
	/**
	 * Constructor.
	 * @param name
	 */
	public AbsractProductionBasedParserTreeNode(Production production) {
		this.production = production;
	}

	public void add(AbsractProductionBasedParserTreeNode node) {
		super.add(node);
	}


	public Production getProduction(){
		return production; 
	}
	
	public String toString(){
		return production.toString();
	}

	public boolean isEmpty() {
		return this.getProduction().isEmpty();
	}

	public boolean isNonTerminal() {
		return this.getProduction().isNonTerminal();
	}

	public boolean isTerminal() {
		return this.getProduction().isTerminal();
	}


	public String getLabel() {
		return this.getProduction().getLabel();
	}
	

}
