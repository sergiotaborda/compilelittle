/**
 * 
 */
package compiler.parser;

import compiler.lexer.Token;

/**
 * 
 */
public class EOFTerminal extends Terminal {

	
	private static EOFTerminal me = new EOFTerminal();
	/**
	 * @return
	 */
	public static EOFTerminal instance() {
		return me;
	}
	
	/**
	 * Constructor.
	 * @param text
	 */
	public EOFTerminal() {
		super(null);
	}


	public String toString(){
		return "EOF";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return "EOF";
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
	
	public boolean isEOF() {
		return true;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(Token token) {
		return token.isEndOfFile();
	}
	
	public boolean equals(Object other){
		return other instanceof EOFTerminal;
	}
	
	public int hashCode(){
		return -1;
	}


}
