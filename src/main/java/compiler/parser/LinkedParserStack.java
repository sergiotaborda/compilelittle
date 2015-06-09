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

	public LinkedParserStack(){}

	LinkedParserStack(LinkedParserStack other){
		this.stack = new LinkedList<>(other.stack);
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


	@Override
	public boolean equals(Object obj) {
		return (obj instanceof LinkedParserStack) && equalsLinkedParserStack((LinkedParserStack)obj); 
	}


	private boolean equalsLinkedParserStack(LinkedParserStack other) {
		return this.stack.size() == other.stack.size() && this.stack.getFirst().equals(other.stack.getFirst()) && this.stack.getLast().equals(other.stack.getLast());
	}





}
