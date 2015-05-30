/**
 * 
 */
package compiler.parser;

import compiler.lexer.Token;

/**
 * 
 */
public class Numeric extends AbstractAutoNonTerminal implements AutoNonTerminal {

	private static Numeric me = new Numeric();
	
	/**
	 * @return
	 */
	public static Numeric instance() {
		return me;
	}
	
	/**
	 * Constructor.
	 * @param name
	 */
	protected Numeric() {
		super("Numeric");
		this.addSemanticAction((p , r) -> {
			 p.setSemanticAttribute("lexicalValue", r.get(0).getSemanticAttribute("lexicalValue").get());
			 p.setSemanticAttribute("node", new NumericNode((String)r.get(0).getSemanticAttribute("lexicalValue").get()));
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNonTerminal() {
		return false;
	}

	public String toString(){
		return "Numeric";
	}
	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void execute(ParserContext ctx, Consumer<ParserContext> tail) {
//		Optional<Token> token = ctx.getMatchToken();
//		if (token.map( t -> macthDigit(t)).orElse(false)){
//			ctx.attach(new NumericNode(this, token.get().getText().get()));
//			ctx.incrementPointer();
//			tail.accept(ctx);
//			ctx.decrementPointer();
//		}
//	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(Token token) {
		return macthDigit(token);
	}
	
	private boolean macthDigit(Token token){
		return token.isNumberLiteral();		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isText() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIdentifier() {
	   return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNumeric() {
		return true;
	}


}
