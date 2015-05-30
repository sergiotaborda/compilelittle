/**
 * 
 */
package compiler.parser;


/**
 * 
 */
public interface ParserStack {

	/**
	 * @param stateStackItem
	 */
	void push(StackItem stackItem);

	/**
	 * 
	 */
	StackItem pop();

	/**
	 * @return
	 */
	StackItem peekFirst();

	/**
	 * @param i
	 * @return
	 */
	StackItem peekSecond();

	/**
	 * @return
	 */
	ParserStack duplicate();
	

}
