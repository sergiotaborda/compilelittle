/**
 * 
 */
package compiler.parser;

import compiler.lexer.Token;

/**
 * 
 */
public class Terminal extends AbstractProduction implements MatchableProduction{

	private String text;

	/**
	 * Constructor.
	 * @param name
	 */
	public Terminal(String text) {
		this.text = text;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final Terminal of(String text) {
		return text.length() == 0 ? EmptyTerminal.instance() : new Terminal(text);
	}

	public String getText(){
		return text;
	}

	public String toString(){
		if (text.charAt(0) == '\n')
		{
			return "'" + text.replaceAll("\n", "\\\\n") + "'";
		} 
		else 
		{
			return "'" + text + "'";
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTerminal() {
		return true;
	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void execute(ParserContext ctx, Consumer<ParserContext> tail) {
//
//		Optional<Token> token = ctx.getMatchToken();
//		if (token.map( t -> t.match(text)).orElse(false)){
//			ctx.attach(new TerminalNode(this));
//			ctx.incrementPointer();
//			tail.accept(ctx);
//			ctx.decrementPointer();
//		}
//
//
//	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(Token token) {
		return token.match(text);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return this.label == null ? this.text : this.label;
	}

	public boolean isEOF() {
		return false;
	}

	public boolean isEOL() {
		return false;
	}

	public boolean equals(Object other){
		return other instanceof Terminal && !((Terminal)other).isEOF() && !((Terminal)other).isEOL() && !((Terminal)other).isEmpty() && ((Terminal)other).text.equals(this.text);
	}
	
	public int hashCode(){
		return text.hashCode();
	}



}
