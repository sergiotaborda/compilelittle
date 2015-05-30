/**
 * 
 */
package compiler.parser.nodes;

import compiler.parser.Terminal;

/**
 * 
 */
public class TerminalNode extends AbsractProductionBasedParserTreeNode {

	/**
	 * Constructor.
	 * @param production
	 */
	public TerminalNode(Terminal production) {
		super(production);
	}
	
	public String getText(){
		return ((Terminal)this.getProduction()).getText();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbsractProductionBasedParserTreeNode duplicate() {
		return new TerminalNode((Terminal)this.getProduction());
	}

}
