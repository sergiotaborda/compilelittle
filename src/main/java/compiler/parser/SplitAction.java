/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import compiler.Grammar;


/**
 * 
 */
public class SplitAction  implements LookupTableAction{


	/**
	 * @param previousAction
	 * @param action
	 * @return
	 */
	public static SplitAction split(LookupTableAction previousAction, LookupTableAction action) {
		if (previousAction instanceof SplitAction){
			SplitAction splitAction = (SplitAction)previousAction;
			splitAction.add(action);
			return splitAction;
		} else {
			SplitAction splitAction = new SplitAction();
			splitAction.add(previousAction);
			splitAction.add(action);
			return splitAction;
		}
	}
	
	private int count = 0;

	@Override
	public boolean isShift() {
		return false;
	}

	@Override
	public boolean isReduce() {
		return false;
	}

	private List<LookupTableAction> list = new ArrayList<>();


	/**
	 * @param action
	 */
	private void add(LookupTableAction action) {
		list.add(action);
	}

	public String toString()
	{
		return "T" + list.toString();
	}

	private boolean split = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LookupTableActionResult operate(Grammar g, ParsingContext ctx) {

		final TokenStackItem currentItem = ctx.inputStream().currentItem();

		final StackItem stateItem = ctx.stack().peekFirst();

		
		if (list.size() == 2 && (list.get(0).isShift() && list.get(1).isReduce() || list.get(1).isShift() && list.get(0).isReduce())){
			// shift reduce conflit
			LookupTableAction shift;
			ReduceAction reduce;
			if (list.get(0).isShift()){
				shift = list.get(0);
				reduce = (ReduceAction)list.get(1);
			} else {
				shift = list.get(1);
				reduce = (ReduceAction)list.get(0);
			}
			Optional<TokenPreference> tokenPref = g.getPreference(currentItem);
			Optional<TokenPreference> productionPref = g.getPreference(ctx.getLastTerminal());

			if (tokenPref.isPresent() && productionPref.isPresent()){
				int comp = productionPref.get().compareTo(tokenPref.get());
				if (comp == 0){
					Optional<Associativity> productionAssociativity = g.getAssociativity(ctx.getLastTerminal());
					if (productionAssociativity.isPresent()){
						if (productionAssociativity.get() == Associativity.LEFT_TO_RIGTH){
							// token is higher : shit
							shift.operate(g, ctx);
							return LookupTableActionResult.Continue;
						} else {
							//reduce what already has been read
							reduce.operate(g, ctx);
							return LookupTableActionResult.Continue;
						}
					} 
				} else if (comp > 0){
					//production is higher
					reduce.operate(g, ctx);
					return LookupTableActionResult.Continue;
				} else {
					// token is higher : shit
					shift.operate(g, ctx);
					return LookupTableActionResult.Continue;
				}
			} else {
				// default
				shift.operate(g, ctx);
				return LookupTableActionResult.Continue;
			}
		} else {
		    // TODO pass to proper logger
//			System.out.print("Conflit at state " + stateItem.toString() + " with token " + currentItem + " ");
//			for (LookupTableAction action : list){
//				System.out.print(((ReduceAction)action).getProductionItem().getProduction().toString());
//				System.out.print( " x ");
//			}
//			System.out.println();
		}
		count++;
		
		if (count > 5){
			((ReduceAction)list.get(0)).operate(g, ctx);
		} else {
			List<ParsingContext> ps = ctx.split(list.size());

			for (int i=0; i < list.size(); i++){
				if (list.get(i).operate(g,ps.get(i)) == LookupTableActionResult.Error){
					ps.get(i).invalidate();
				}
			}
		}

		return LookupTableActionResult.Continue;
	}


}
