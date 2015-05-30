/**
 * 
 */
package compiler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;


/**
 * 
 */
public class FuturePromisseSet<T> implements PromisseSet<T> {

	List<PromisseSet<T>> sets = new ArrayList<PromisseSet<T>>();
	private boolean isRealizing = false;
	
	/**
	 * @param set
	 * @return
	 */
	public static <X> FuturePromisseSet<X> promisse(PromisseSet<X> set) {
		return new FuturePromisseSet<>(set);
	}
	
	FuturePromisseSet (){

	}
	
	private FuturePromisseSet (List<PromisseSet<T>> all){
		this.sets = all;
	}
	
	private FuturePromisseSet (PromisseSet<T> a){
		this.sets.add(a);
	}
	
	private FuturePromisseSet (PromisseSet<T> a , PromisseSet<T> b){
		this.sets.add(a);
		this.sets.add(b);
	}
	
	public String toString(){
		return "F" + sets.toString();
	}
	 
	/**
	 * {@inheritDoc}
	 */
	public void put(PromisseSet<T> other) {
		if (other == this){
			return; // no-op
		}
		if (!sets.contains(other)){
			sets.add(other);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PromisseSet<T> realise() {

		if (this.isRealizing){
			return new EmptyPromisseSet<T>();
		}
		
		this.isRealizing = true;
		RealizedPromisseSet<T> realised = new RealizedPromisseSet<>();
		
		Queue<PromisseSet<T>> all = new LinkedList<>();
		
		all.addAll(sets);
		
		while (!all.isEmpty()){
			
			PromisseSet<T> p = all.poll();
			
			if (p.isEmpty()){
				continue;
			}
			if (p == this){
				continue;
			}
			if (p.isRealised()){
				for(T m : p){
					realised.add(m);
				}

			} else {
				all.add(p.realise());	
			}
		}
		
		this.isRealizing = false;
		
		return realised;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRealised() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean match(Function<T, Boolean> predicate) {
	
		for (PromisseSet<T> s : sets){
			if (s.match(predicate)){
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(T instance) {
		for (PromisseSet<T> s : sets){
			if (s.contains(instance)){
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(T instance) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}


	@Override
	public PromisseSet<T> subtract(T other) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	@Override
	public PromisseSet<T> intersect(PromisseSet<T> other) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PromisseSet<T> simplify() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PromisseSet<T> union(PromisseSet<T> other) {
		throw new UnsupportedOperationException("Not implememented yet");
	}




	
	

}
