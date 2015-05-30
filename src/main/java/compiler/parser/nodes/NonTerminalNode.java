/**
 * 
 */
package compiler.parser.nodes;

import compiler.parser.NonTerminal;
import compiler.parser.Production;

/**
 * 
 */
public class NonTerminalNode extends AbsractProductionBasedParserTreeNode {

	/**
	 * Constructor.
	 * @param production
	 */
	public NonTerminalNode(Production production) {
		super(production);	
	}

	
	public String getName (){
		Production p = this.getProduction();
		return p == null ? "" : ((NonTerminal)p).getName();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbsractProductionBasedParserTreeNode duplicate() {
		return new NonTerminalNode(this.getProduction());
	}



}
