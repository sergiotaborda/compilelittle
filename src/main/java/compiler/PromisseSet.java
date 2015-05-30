/**
 * 
 */
package compiler;

import java.util.function.Function;


/**
 * 
 */
public interface PromisseSet<T> extends Iterable<T>{

	public PromisseSet<T> simplify();
	
	public PromisseSet<T>  union(PromisseSet<T> other);
	
	public PromisseSet<T> realise();
	
	public boolean isRealised();

	public boolean match (Function<T, Boolean> predicate);

	/**
	 * @param instance
	 * @return
	 */
	public boolean contains(T instance);

	/**
	 * @param instance
	 */
	public boolean remove(T instance);

	/**
	 * @return
	 */
	public boolean isEmpty();

	public PromisseSet<T> subtract(T other);

	public PromisseSet<T> intersect(PromisseSet<T> other);


}
