package compiler.parser;

import compiler.Grammar;

public class GotoAction implements LookupTableAction {

	private int stateId;

	public GotoAction(int stateId) {
		this.stateId = stateId;
	}

	public String toString(){
		return "G" +stateId;
	}

	@Override
	public LookupTableActionResult operate(Grammar g, ParsingContext ctx) {
		
		ctx.stack().push(new StateStackItem(stateId));
		
		return LookupTableActionResult.Continue;
	}

	/**
	 * @return
	 */
	public int getStateId() {
		return stateId;
	}
	
	@Override
	public boolean isShift() {
		return false;
	}

	@Override
	public boolean isReduce() {
		return false;
	}
}
