package compiler.parser;

public class StateStackItem implements StackItem {

	private int stateId;

	public StateStackItem(int stateId) {
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}


	public String toString(){
		return Integer.toString(stateId);
	}


	@Override
	public boolean equals(Object obj) {
		return (obj instanceof StateStackItem) && equalsStateStackItem((StateStackItem)obj); 
	}


	private boolean equalsStateStackItem(StateStackItem other) {
		return this.stateId == other.stateId;
	}
	
	public int hashCode(){
		return this.stateId;
	}
}
