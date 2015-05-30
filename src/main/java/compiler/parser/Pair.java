/**
 * 
 */
package compiler.parser;

/**
 * 
 */
public class Pair<A,B> {

	public A item1;
	public B item2;

	/**
	 * Constructor.
	 * @param p
	 * @param nextReadable
	 */
	public Pair(A a, B b) {
		this.item1 = a;
		this.item2 = b;
	}

}
