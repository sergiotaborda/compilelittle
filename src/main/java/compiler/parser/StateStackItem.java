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
}
