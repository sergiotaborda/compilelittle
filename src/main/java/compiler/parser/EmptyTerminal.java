/**
 * 
 */
package compiler.parser;

import compiler.lexer.Token;

/**
 * 
 */
public class EmptyTerminal extends Terminal {

	
	private static EmptyTerminal me = new EmptyTerminal();
	/**
	 * @return
	 */
	public static EmptyTerminal instance() {
		return me;
	}
	
	/**
	 * Constructor.
	 * @param text
	 */
	public EmptyTerminal() {
		super("");
	}

	public String toString(){
		return "£";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return "£";
	}
	
	public boolean isEmpty(){
		return true;
	}
	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void execute(ParserContext ctx, Consumer<ParserContext> tail) {
//		tail.accept(ctx);
//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(Token token) {
		return true;
	}
	
	public boolean equals(Object other){
		return other instanceof EmptyTerminal;
	}
	
	public int hashCode(){
		return 0;
	}

}
