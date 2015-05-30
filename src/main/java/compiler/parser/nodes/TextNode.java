/**
 * 
 */
package compiler.parser.nodes;

import compiler.parser.Text;

/**
 * 
 */
public class TextNode extends AbsractProductionBasedParserTreeNode {

	private String text;

	/**
	 * Constructor.
	 * @param production
	 */
	public TextNode(Text production, String text) {
		super(production);
		this.text = text;
		
	}
	
	public String getText(){
		return text;
	}
	
	public String toString(){
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbsractProductionBasedParserTreeNode duplicate() {
		return new TextNode((Text) this.getProduction(), text);
	}

}
