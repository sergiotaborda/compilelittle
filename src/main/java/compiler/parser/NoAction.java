package compiler.parser;

import compiler.Grammar;

public class NoAction implements LookupTableAction{

	public String toString(){
		return "NOACTION";
	}

	@Override
	public LookupTableActionResult operate(Grammar g, ParsingContext ctx) {
		return LookupTableActionResult.Error;
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
