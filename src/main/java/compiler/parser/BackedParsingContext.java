/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import compiler.lexer.TokenStream;
import compiler.parser.nodes.ParserTreeNode;

/**
 * 
 */
public class BackedParsingContext implements ParsingContext {

	private static int nextId =0;
	private int id;
	
	private TokenStreamStackInputStreamAdapter input;
	ParserStack stack;
	private boolean valid = true;
	private ParsingContextBag parsingContextBag;
	private LinkedList<DeferedSemanticAction> deferredSemanticActions;

	/**
	 * Constructor.
	 * @param parsingContextBag
	 * @param tokens
	 */
	public BackedParsingContext(ParsingContextBag parsingContextBag, TokenStream tokens) {
		this(parsingContextBag,new LinkedParserStack(), new TokenStreamStackInputStreamAdapter(tokens), new LinkedList<>());
		stack.push(new StateStackItem(0));
	}
	
	BackedParsingContext(ParsingContextBag parsingContextBag,ParserStack stack, TokenStreamStackInputStreamAdapter input,List<DeferedSemanticAction> deferredSemanticActions  ) {
		this.parsingContextBag = parsingContextBag;
		this.stack = stack;
		this.input = input;
		this.deferredSemanticActions = new LinkedList<>(deferredSemanticActions);
		this.id = nextId++;
		
	}
	

	public String toString(){
		return stack.toString() + "\n" + input.toString();
	}
	
	public int hashCode() {
		return deferredSemanticActions.size();
	};
	
	public boolean equals(Object other){
		return other instanceof BackedParsingContext && equals((BackedParsingContext)other);
	}
	
	private boolean equals(BackedParsingContext other){
		return other.id == this.id || 
				(deferredSemanticActions.size() == other.deferredSemanticActions.size() 
				&& input.equals(other.input)
				&& stack.equals(other.stack)
				);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParserStack stack() {
		return stack;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StackInputStream inputStream() {
		return input;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ParsingContext> split(int size) {
		List<ParsingContext> newContexts  = new ArrayList<>(size);
		for (int i =0; i< size; i++){
			BackedParsingContext b = new BackedParsingContext(parsingContextBag, stack.duplicate(), input.duplicate(), this.deferredSemanticActions);		
			newContexts.add(b);
		}
		parsingContextBag.replace(this, newContexts);
		return newContexts;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void invalidate() {
		valid = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		return valid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParserTreeNode accept() {
		
		executeDeferedSemanticActions();
		
		return (ParserTreeNode) stack.pop();
	}

	
	
	/**
	 * 
	 */
	private void executeDeferedSemanticActions() {
		for (DeferedSemanticAction action : deferredSemanticActions){
			action.execute();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDeferedSemanticAction(ProductionItem target, ProductionStackItem left, LinkedList<Symbol> read) {
		deferredSemanticActions.add(new DeferedSemanticAction(target, left, read));
	}

	@Override
	public SemanticStackItem getLastTerminal() {
		ListIterator<DeferedSemanticAction> it = deferredSemanticActions.listIterator(deferredSemanticActions.size());
		
		while (it.hasPrevious()){
			DeferedSemanticAction p = it.previous();
			Deque<SemanticStackItem> stack = new LinkedList<>();
			for (Symbol s : p.getRead()){
				stack.push((SemanticStackItem)s);
				
				while (!stack.isEmpty()){
					SemanticStackItem ssi = stack.pop();
					if (ssi instanceof TokenStackItem ){
						if (((TokenStackItem)ssi).getToken().isOperator()){
							return ssi;
						}
					} else {
						for ( ParserTreeNode a : ((ProductionStackItem) ssi).getChildren()){
							stack.push((SemanticStackItem) a);
						}
					}
				}
				
			}

		}
		
		return null;
	}

}
