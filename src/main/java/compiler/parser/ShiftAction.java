package compiler.parser;


public class ShiftAction implements LookupTableAction {

	private int stateId;

	public ShiftAction(int stateId) {
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}
	
	public String toString(){
		return "S" + stateId;
	}

	@Override
	public LookupTableActionResult operate(ParsingContext ctx) {
		ctx.stack().push(ctx.inputStream().currentItem());
		ctx.stack().push(new StateStackItem(stateId));
		
		ctx.inputStream().moveNext();
		
		return LookupTableActionResult.Continue;
	}
}
