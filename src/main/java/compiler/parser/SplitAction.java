/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.List;


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
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public LookupTableActionResult operate(ParsingContext ctx) {
		
		List<ParsingContext> ps = ctx.split(list.size());

		for (int i=0; i < list.size(); i++){
			if (list.get(i).operate(ps.get(i)) == LookupTableActionResult.Error){
				ps.get(i).invalidate();
			}
		}
		
		return LookupTableActionResult.Continue;
	}


}
