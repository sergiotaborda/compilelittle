/**
 * 
 */
package compiler.parser;

import compiler.lexer.Token;

/**
 * 
 */
public class EOLTerminal extends Terminal {

	
	private static EOLTerminal me = new EOLTerminal();
	/**
	 * @return
	 */
	public static EOLTerminal instance() {
		return me;
	}
	
	/**
	 * Constructor.
	 * @param text
	 */
	public EOLTerminal() {
		super(null);
	}


	public String toString(){
		return "EOL";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return "EOL";
	}
	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void execute(ParserContext ctx, Consumer<ParserContext> tail) {
//		
//			if (ctx.matchEOL()){
//				ctx.incrementPointer();
//				tail.accept(ctx);
//				ctx.decrementPointer();
//				
//			}
//		
//		
//	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(Token token) {
		return token.isEndOfLine();
	}

	public boolean isEOL() {
		return true;
	}

	public boolean equals(Object other){
		return other instanceof EOLTerminal;
	}
	
	public int hashCode(){
		return -2;
	}
}
