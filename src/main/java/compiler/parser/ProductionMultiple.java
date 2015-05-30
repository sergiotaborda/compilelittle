/**
 * 
 */
package compiler.parser;


/**
 * 
 */
public class ProductionMultiple extends AbstractProduction {

	
	private Production template;
	private boolean allowEmptyMatch;

	public ProductionMultiple(Production template, boolean allowEmptyMath){
		this.template = template;
		this.allowEmptyMatch = allowEmptyMath;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return template.getLabel();
	}

	
	public Production getTemplate(){
		return template;
	}
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void execute(ParserContext ctx, Consumer<ParserContext> tail) {
//		
//		while(true){
//			ParserContext newCtx = ctx.duplicate(template);
//			template.execute(newCtx, tail);
//			if (!newCtx.isDerivationComplete()){
//				if (this.allowEmptyMatch){
//					break;
//				} else {
//					return;
//				}
//			} else {
//				ctx.merge(newCtx);
//				ctx.attach(newCtx.popRule());
//			}
//		}
//		tail.accept(ctx);
//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMultiple() {
		return true;
	}

}
