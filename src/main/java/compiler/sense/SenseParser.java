/**
 * 
 */
package compiler.sense;

import compiler.lexer.TokenStream;
import compiler.parser.BottomUpParser;
import compiler.parser.nodes.ParserTreeNode;

/**
 * 
 */
public class SenseParser extends BottomUpParser {

	/**
	 * Constructor.
	 * @param senseLanguage
	 */
	public SenseParser(SenseLanguage senseLanguage) {
		super(senseLanguage);
	}
	
	public ParserTreeNode parse(TokenStream tokens) {
		return super.parse(new SenseAwareTokenStream(tokens));
	}

}
