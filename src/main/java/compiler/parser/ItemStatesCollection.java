/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 
 */
public class ItemStatesCollection {

	Map<Key, ItemState> states = new HashMap<>();
	
	/**
	 * @param state
	 */
	public void add(ItemState state) {
		states.put(new Key(state),state);
	}

	/**
	 * @return
	 */
	public List<ItemState> toList() {
		 List<ItemState> s = new ArrayList<ItemState>(states.size());
		 
		 for (ItemState it : states.values()){
			 s.add(it);
		 }
		 return s;
	}

	/**
	 * @param newState
	 * @return
	 */
	public Optional<ItemState> find(ItemState other) {
		return Optional.ofNullable(states.get(new Key(other)));
	}

	public String toString(){
		return states.values().toString();
	}
	
	private static class Key {
		
		ItemState state;
		
		public Key(ItemState state){
			this.state = state;
		}
		
		public int hashCode(){
			return state.items.size();
		}
		
		public boolean equals(Object other){
			return ((Key)other).state.itemsEquals(this.state); 
		}

	}
}
