package compiler.parser;

import java.util.Iterator;
import java.util.Map;

import compiler.Grammar;
import compiler.lexer.Token;
import compiler.lexer.TokenStream;
import compiler.parser.LookupTableAction.LookupTableActionResult;
import compiler.parser.nodes.ParserTreeNode;

public class BottomUpParser implements Parser{

	private Grammar grammar;
	private ItemStateAutomatonFactory itemStateAutomatonFactory;

	public BottomUpParser(Grammar grammar, ItemStateAutomatonFactory itemStateAutomatonFactory){
		this.grammar = grammar;
		this.itemStateAutomatonFactory = itemStateAutomatonFactory;
	}

	public LookupTable getLookupTable(){ // TODO cache the table
		return itemStateAutomatonFactory.create().produceLookupTable(grammar);	
	}

	@Override
	public ParserTreeNode parse(TokenStream tokens) {

		LookupTable table = this.getLookupTable();


		ParsingContextBag bag = new ParsingContextBag(tokens);


		ParserTreeNode root = null;

		outter : while(!bag.isEmpty())
		{
			for (Iterator<ParsingContext> it = bag.iterator(); it.hasNext(); ){
				ParsingContext ctx = it.next();

				StackItem stateItem = ctx.stack().peekFirst();
// TODO  grammar for class with method with empty block 
				final TokenStackItem currentItem = ctx.inputStream().currentItem();
				LookupTableAction action = table.getAction(stateItem,currentItem);

			//	System.out.println("[" +  ctx.hashCode() + "] with " + currentItem + " performed action " + action.toString());
				LookupTableActionResult res = action.operate(ctx);
				
				
				
				if (res == LookupTableActionResult.Accept){
					root =  ctx.accept();
					
					break outter;
			    } else if(res == LookupTableActionResult.Error) {
			    	ctx.invalidate();
			    	if (bag.size() == 1){
			    		Token token = currentItem.getToken();
			    		if (token.isEndOfFile()){
			    			throw new RuntimeException("Unexpected EOF (state " + stateItem + " )");
			    		}
			    		LookupTableRow row = table.stream().filter(r -> r.getId() == ((StateStackItem)stateItem).getStateId()).findFirst().get();
			    		
			    		StringBuilder s = new StringBuilder();
			    		for (Map.Entry<Production, LookupTableAction> m : row){
			    			if (!(m.getValue() instanceof ExceptionAction) && m.getKey().isTerminal()){
			    				s.append(m.getKey());
			    				s.append(", ");
			    			}
			    			
			    		}
			    		s.deleteCharAt(s.length()-1);
			    		s.deleteCharAt(s.length()-1);
			    		throw new RuntimeException("Token: '" +  token.getText().get() + "' is not excepted at position " + token.getPosition().getLineNumber() + ":" + token.getPosition().getColumnNumber() + " expected one of " + s + "(state " + stateItem + " )");

			    	}
				}
			}
			bag.reset();
		}

		return root;
	}



}
