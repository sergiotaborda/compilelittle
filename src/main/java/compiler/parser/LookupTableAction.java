package compiler.parser;


public interface LookupTableAction {

	public enum LookupTableActionResult {
		Accept,
		Continue,
		Error
		
	}
	
	
	LookupTableActionResult operate(ParsingContext ctx);

}
