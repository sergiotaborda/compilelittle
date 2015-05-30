/**
 * 
 */
package compiler.parser;

import compiler.lexer.Token;

/**
 * 
 */
public class Identifier extends AbstractAutoNonTerminal {

	private static Identifier me = new Identifier();
	
	/**
	 * @return
	 */
	public static Identifier instance() {
		return me;
	}
	
	/**
	 * Constructor.
	 * @param name
	 */
	protected Identifier() {
		super("Identifier");
		this.addSemanticAction((p , r) -> {
			 p.setSemanticAttribute("lexicalValue", r.get(0).getSemanticAttribute("lexicalValue").get());
			 p.setSemanticAttribute("node", new IdentifierNode((String)r.get(0).getSemanticAttribute("lexicalValue").get()));
		});
	}
	
	public String toString(){
		return "ID";
	}



//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void execute(ParserContext ctx, Consumer<ParserContext> tail) {
//		Optional<Token> token = ctx.getMatchToken();
//		if (token.map( t -> t.isId()).orElse(false)){
//			ctx.attach(new IdentifierNode(this, token.get().getText().get()));
//			ctx.incrementPointer();
//			tail.accept(ctx);	
//		}
//	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(Token token) {
		return token.isId();
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
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNumeric() {
		return false;
	}

}
