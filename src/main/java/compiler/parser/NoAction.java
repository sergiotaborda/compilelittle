package compiler.parser;


public class NoAction implements LookupTableAction{

	public String toString(){
		return "NOACTION";
	}

	@Override
	public LookupTableActionResult operate(ParsingContext ctx) {
		return LookupTableActionResult.Error;
	}
}
