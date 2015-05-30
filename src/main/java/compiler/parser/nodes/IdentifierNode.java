/**
 * 
 */
package compiler.parser.nodes;

import compiler.parser.Identifier;

/**
 * 
 */
public class IdentifierNode extends AbsractProductionBasedParserTreeNode {

	private String id;

	/**
	 * Constructor.
	 * @param production
	 */
	public IdentifierNode(Identifier production, String id) {
		super(production);
		this.id = id;
		
	}
	
	public String getId(){
		return id;
	}
	
	public String toString(){
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbsractProductionBasedParserTreeNode duplicate() {
		return new IdentifierNode((Identifier) this.getProduction(), id);
	}


}
