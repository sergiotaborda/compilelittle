package compiler.parser;


public class GotoAction implements LookupTableAction {

	private int stateId;

	public GotoAction(int stateId) {
		this.stateId = stateId;
	}

	public String toString(){
		return "G" +stateId;
	}

	@Override
	public LookupTableActionResult operate(ParsingContext ctx) {
		
		ctx.stack().push(new StateStackItem(stateId));
		
		return LookupTableActionResult.Continue;
	}

	/**
	 * @return
	 */
	public int getStateId() {
		return stateId;
	}
}
