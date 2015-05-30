package compiler.parser;



public class AcceptAction implements LookupTableAction {


	public AcceptAction() {
	}

	public String toString(){
		return "A";
	}

	@Override
	public LookupTableActionResult operate(ParsingContext ctx) {
		
		ctx.stack().pop(); // removes the item so the goal state is ate the top of the stack

		return LookupTableActionResult.Accept;
	}
}
