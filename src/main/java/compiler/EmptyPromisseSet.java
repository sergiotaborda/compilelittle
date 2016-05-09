/**
 * 
 */
package compiler;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

/**
 * 
 */
public class EmptyPromisseSet<T> implements PromisseSet<T> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return Collections.<T>emptySet().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PromisseSet<T> realise() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRealised() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(Function<T, Boolean> predicate) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(T instance) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(T instance) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return true;
	}

	public String toString(){
		return "";
	}


	@Override
	public PromisseSet<T> subtract(T other) {
		return this;
	}

	@Override
	public PromisseSet<T> intersect(PromisseSet<T> other) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PromisseSet<T> simplify() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PromisseSet<T> union(PromisseSet<T> other) {
		return other;
	}



}
