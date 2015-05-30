package compiler.parser;

import java.util.LinkedList;

import compiler.parser.nodes.ParserTreeNode;

public class ReduceAction implements LookupTableAction {

	private ProductionItem target;
	private LookupTable table;

	public ReduceAction(ProductionItem target, LookupTable table) { 
		this.target = target;
		this.table = table;
	}

	public String toString(){
		return "R[" + target.toString() + "]";
	}

	@Override
	public LookupTableActionResult operate(ParsingContext ctx) {

		try {
			ProductionStackItem left = new ProductionStackItem(target.root);

			ParserStack stack = ctx.stack();

			int count = target.productions.size();


			LinkedList<Symbol> read = new LinkedList<>();

			for (int i =0; i < count * 2; i++){

				StackItem item = stack.pop();
				if (i % 2 == 1){
					left.add((ParserTreeNode) item);
					read.addFirst((Symbol)item);
				}
			}


			if (count != 0){
				ctx.addDeferedSemanticAction(target, left, read);
			}
			
			stack.push(left);

			StackItem p = stack.peekFirst();
			StackItem stateItem = stack.peekSecond();

			LookupTableAction gotoAction = table.getAction(stateItem,p);
			if (gotoAction instanceof GotoAction){
				//System.out.println("["  + ctx.hashCode() + "] performed action " + gotoAction.toString());
				gotoAction.operate(ctx);
			} else {
				return LookupTableActionResult.Error;
			}


			return LookupTableActionResult.Continue;
		} catch (Exception e){
			return LookupTableActionResult.Error;
		}

	}
}
