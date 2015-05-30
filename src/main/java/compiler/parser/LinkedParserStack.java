/**
 * 
 */
package compiler.parser;

import java.util.LinkedList;

/**
 * 
 */
public class LinkedParserStack implements ParserStack {

	LinkedList<StackItem> stack = new LinkedList<StackItem>();
	boolean valid = true;
	
	public LinkedParserStack(){}
	
	LinkedParserStack(LinkedParserStack other){
		this.stack = new LinkedList<>(other.stack);
		this.valid = other.valid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void push(StackItem stackItem) {
		stack.push(stackItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StackItem pop() {
		return stack.pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StackItem peekFirst() {
		return stack.peek();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StackItem peekSecond() {
		return stack.get(1);
	}

	
	public String toString(){
		return stack.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParserStack duplicate() {
	 return new LinkedParserStack(this);
	}



}
