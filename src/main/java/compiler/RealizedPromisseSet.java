/**
 * 
 */
package compiler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 
 */
public class RealizedPromisseSet<T> implements PromisseSet<T>{

	private Set<T> set;
	
	public RealizedPromisseSet(T first, T ... items ){
		this();
		this.set.add(first);
		
		for (T t : items){
			this.set.add(t);
		}
	}
	
//	public RealizedPromisseSet(PromisseSet<T> other){
//		this();
//		this.addAll(other);
//	}
	
	public RealizedPromisseSet(){
		this.set = new HashSet<>();
	}
	
	public RealizedPromisseSet(Set<T> set ){
		this.set = set;
	}
	
	public String toString(){
		return "R" + set.toString();
	}
	
	public void add(T item){
		this.set.add(item);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RealizedPromisseSet<T>  union(PromisseSet<T> other) {
		if (other == null || other.isEmpty()){
			return this;
		}
		if (this.isEmpty() && other.isRealised()){
			return (RealizedPromisseSet<T>)other;
		}
		if (other.isRealised()){
			RealizedPromisseSet<T> res = new RealizedPromisseSet<T>();
			
			res.set.addAll(this.set);
			
			RealizedPromisseSet<T> o = (RealizedPromisseSet<T>)other;
			
			res.set.addAll(o.set);
			
			return res.simplify();

		} else {
			throw new RuntimeException("cannot unite");
		}
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
		
		for (T t : set){
			if (predicate.apply(t)){
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
		return set.contains(instance);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(T instance) {
		return set.remove(instance);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return set.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public RealizedPromisseSet<T> subtract(T other) {
		RealizedPromisseSet<T> set = new RealizedPromisseSet<>();
		
		for(T p : this.set){
			if (!p.equals(other)){
				set.add(p);
			}
		}
		
		return set;
	}

	
	public int hashCode(){
		return set.size();
	}
	
	public boolean equals(Object other){
		return equals(this.set, ((RealizedPromisseSet)other).set);
	}

	private boolean equals(Set<T> a, Set<T> b) {
		if (a == b){
			return true; // same
		}
		
		if (a.size() != b.size()){
			return false;
		}
		
		if (a.size() == 0){
			return true; // both empty or same
		}
		
		return a.containsAll(b);
		
	}

	@Override
	public RealizedPromisseSet<T> intersect(PromisseSet<T> other) {
		if (other.isEmpty()){
			return new RealizedPromisseSet<T>();
		} else {
			RealizedPromisseSet<T> result = new RealizedPromisseSet<>();
			
			for (T t : set){
				if (other.contains(t)){
					result.add(t);
				}
			}
			
			return result;
		}
	}

	/**
	 * @return
	 */
	public int size() {
		return set.size();
	}

	private static Map<RealizedPromisseSet, RealizedPromisseSet> cannon = new HashMap<>();
	
	/**
	 * @return
	 */
	public RealizedPromisseSet<T> simplify() {
		RealizedPromisseSet set = cannon.get(this);
		if (set == null){
			cannon.put(this, this);
			return this;
		}
		return set;
	}



}
