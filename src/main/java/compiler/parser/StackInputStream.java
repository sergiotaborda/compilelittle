/**
 * 
 */
package compiler.parser;


/**
 * 
 */
public interface StackInputStream {

	public void moveNext();
	public boolean hasNext();
	public TokenStackItem currentItem();

}
