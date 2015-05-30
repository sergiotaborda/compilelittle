/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 */
public class ProductionSequence extends AbstractProduction implements Iterable<Production>{

	
	List<Production> sequence = new ArrayList<>(3);
	

	public ProductionSequence(Production first){
		sequence.add(first);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductionSequence add(Production other) {
		sequence.add(other);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSequence() {
		return true;
	}

	public Production get(int index){
		return sequence.get(index);
	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void execute(ParserContext ctx, Consumer<ParserContext> tail) {
//		for(int i =0; i < sequence.size();i++){
//			Production currentProduction = sequence.get(i);
//			ParserContext newCtx = ctx.duplicate(currentProduction);
//			currentProduction.execute(newCtx, tail);
//			if (!newCtx.isDerivationComplete()){
//				return;
//			} else {
//				ctx.merge(newCtx);
//				ctx.attach(newCtx.popRule());
//			}
//		}
//		tail.accept(ctx);
//	}

	public String toString(){
		return this.getLabel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return this.label == null ? sequence.toString(): this.label;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Production> iterator() {
		return sequence.iterator();
	}


	/**
	 * @param i
	 */
	public Production remove(int index) {
		return sequence.remove(index);
	}

	/**
	 * @return
	 */
	public int size() {
		return sequence.size();
	}

	public Production getFirst() {
		return sequence.get(0);
	}


}
