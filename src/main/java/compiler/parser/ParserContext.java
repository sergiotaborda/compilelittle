/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import compiler.lexer.Token;
import compiler.lexer.TokenStream;
import compiler.parser.nodes.AbsractProductionBasedParserTreeNode;
import compiler.parser.nodes.NonTerminalNode;
import compiler.parser.nodes.ParserTreeNode;
import compiler.parser.nodes.TerminalNode;

/**
 * 
 */
@Deprecated
public class ParserContext {

	private List<Token> input;
    private int pointer = 0;
    private Deque<ParserTreeNode> stack = new LinkedList<>();
	private boolean derivation = false;
    
	ParserContext(List<Token> tokens){
		this.input = tokens;
		
	}
	
	public ParserContext(TokenStream tokens){
		this.input = new ArrayList<Token>();
		
		while(tokens.hasNext()){
			input.add(tokens.next());
		}
		
	}
	
	public String toString (){
		return stack.toString();
	}
	
	/**
	 * Obtains {@link int}.
	 * @return the pointer
	 */
	public int getPointer() {
		return pointer;
	}

	public void incrementPointer(){
		pointer++;
	}
	
	public void decrementPointer(){
		//pointer--;
	}
	
	public void pushRule(Production p){
		if (p.isTerminal()){
			Terminal t = (Terminal)p;
			stack.push(new TerminalNode(t));
		} else {
			stack.push(new NonTerminalNode(p));
		}  
		
	}
	
	public void PushRule(AbsractProductionBasedParserTreeNode node){
		stack.push(node);
	}
	
	public ParserTreeNode popRule(){
		return stack.pop();
	}
	
	public void attach(ParserTreeNode node){
		if (stack.isEmpty()){
			stack.add(node);
		} else {
			stack.peek().add(node);
		}
	}

	/**
	 * @param text
	 * @return
	 */
	public boolean match(String text) {
		if (pointer >= input.size()){
			return false;
		}
		return input.get(pointer).match(text);
	}

	/**
	 * @return
	 */
	public boolean matchEOF() {
		return pointer < input.size() && input.get(pointer).isEndOfFile();
	}
	/**
	 * @return
	 */
	public boolean matchEOL() {
		return pointer < input.size() && input.get(pointer).isEndOfLine();
	}
	
	/**
	 * @return
	 */
	public boolean matchId() {
		return pointer < input.size() && input.get(pointer).isId();
	}

	public Optional<Token> getMatchToken(){
		if ( pointer < input.size()){
			return Optional.of(input.get(pointer));
		} else {
			return Optional.empty();
		}
	}
	
	public Optional<Token> getNextMatchToken(){
		if ( pointer + 1< input.size()){
			return Optional.of(input.get(pointer + 1));
		} else {
			return Optional.empty();
		}
	}
	
	public void markDerivationComplete(){
		this.derivation = true;
	}

	public boolean isDerivationComplete(){
		return derivation;
	}
	
	/**
	 * @param currentProduction 
	 * @return
	 */
	public ParserContext duplicate(Production currentProduction) {
		ParserContext ctx = new ParserContext(input);
		ctx.pushRule(currentProduction);
		ctx.pointer = this.pointer;
		return ctx;
	}

	/**
	 * @param newCtx
	 */
	public void merge(ParserContext other) {
	
		this.derivation = other.derivation;
		this.pointer = other.pointer;
//		
//		ParserTreeNode otherRoot = other.stack.peek();
//		ParserTreeNode myRoot = this.stack.peek();
//			
//		myRoot.add(otherRoot);
		
	}








	
	
}
