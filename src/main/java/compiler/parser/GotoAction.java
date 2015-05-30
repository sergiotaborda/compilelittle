package compiler.parser;


public class GotoAction implements LookupTableAction {

	private ItemState state;

	public GotoAction(ItemState state) {
		this.state = state;
	}

	public String toString(){
		return "G" + state.getId();
	}

	@Override
	public LookupTableActionResult operate(ParsingContext ctx) {
		
		ctx.stack().push(new StateStackItem(state.getId()));
		
		return LookupTableActionResult.Continue;
	}
}
