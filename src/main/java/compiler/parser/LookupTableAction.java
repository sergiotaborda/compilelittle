package compiler.parser;

import compiler.Grammar;

public interface LookupTableAction {

	public enum LookupTableActionResult {
		Accept,
		Continue,
		Error
		
	}
	
	public boolean isShift();
	public boolean isReduce();
	LookupTableActionResult operate(Grammar g, ParsingContext ctx);

}
