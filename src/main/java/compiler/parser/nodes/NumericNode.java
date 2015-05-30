/**
 * 
 */
package compiler.parser.nodes;

import compiler.parser.Numeric;

/**
 * 
 */
public class NumericNode extends AbsractProductionBasedParserTreeNode {

	private String text;

	/**
	 * Constructor.
	 * @param production
	 */
	public NumericNode(Numeric production, String text) {
		super(production);
		this.text = text;
		
	}
	
	public String getText(){
		return text;
	}

	/**
	 * @return
	 */
	public long getLong() {
		return Long.parseLong(text);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbsractProductionBasedParserTreeNode duplicate() {
		return new NumericNode((Numeric)this.getProduction(), this.text);
	}

}
