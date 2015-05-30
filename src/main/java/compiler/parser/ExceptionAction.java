package compiler.parser;


public class ExceptionAction implements LookupTableAction {

	

	public ExceptionAction(int id){

	}
	
	public String toString(){
		return "E";
	}
	
	@Override
	public LookupTableActionResult operate(ParsingContext ctx ) {

//		Token token = ctx.inputStream().currentItem().getToken();
//		if (token.isEndOfFile()){
//			throw new RuntimeException("Unexpected EOF (state " + stateId + " )");
//		}
//		throw new RuntimeException("Token: " +  token.getText().get() + " is not excepted at position " + token.getPosition().getLineNumber() + ":" + token.getPosition().getColumnNumber() + "(state " + stateId + " )");
		
		return LookupTableActionResult.Error;
	}

}
