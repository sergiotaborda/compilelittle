/**
 * 
 */
package compiler.parser;

import compiler.lexer.Token;

/**
 * 
 */
public class Text extends AbstractAutoNonTerminal implements AutoNonTerminal{

	private static Text me = new Text();
	
	/**
	 * @param string 
	 * @return
	 */
	public static Text instance() {
		return me;
	}
	
	/**
	 * Constructor.
	 * @param name
	 */
	protected Text() {
		super("Text");
		this.addSemanticAction((p , r) -> {
			 p.setSemanticAttribute("lexicalValue", r.get(0).getSemanticAttribute("lexicalValue").get());
			 p.setSemanticAttribute("node", new TextNode((String)r.get(0).getSemanticAttribute("lexicalValue").get()));
		});
	}
	
	public String toString(){
		return "Text";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNonTerminal() {
		return false;
	}

//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void execute(ParserContext ctx, Consumer<ParserContext> tail) {
//		Optional<Token> token = ctx.getMatchToken();
//		if (token.map( t -> t.isStringLiteral()).orElse(false)){
//			ctx.attach(new TextNode(this, token.get().getText().get()));
//			ctx.incrementPointer();
//			tail.accept(ctx);	
//		}
//	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(Token token) {
		return token.isStringLiteral();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isText() {
		return true;
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
		return false;
	}






}
