package compiler.parser;


public class ShiftAction implements LookupTableAction {

	private ItemState state;

	public ShiftAction(ItemState state) {
		this.state = state;
	}

	public String toString(){
		return "S" + state.getId();
	}

	@Override
	public LookupTableActionResult operate(ParsingContext ctx) {
		ctx.stack().push(ctx.inputStream().currentItem());
		ctx.stack().push(new StateStackItem(state.getId()));
		
		ctx.inputStream().moveNext();
		
		return LookupTableActionResult.Continue;
	}
}
