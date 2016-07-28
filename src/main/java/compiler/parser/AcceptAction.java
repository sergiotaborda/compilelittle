package compiler.parser;

import compiler.Grammar;

public class AcceptAction implements LookupTableAction {


	public AcceptAction() {
	}

	public String toString(){
		return "A";
	}

	@Override
	public LookupTableActionResult operate(Grammar g,ParsingContext ctx) {
		
		ctx.stack().pop(); // removes the item so the goal state is ate the top of the stack

		return LookupTableActionResult.Accept;
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
