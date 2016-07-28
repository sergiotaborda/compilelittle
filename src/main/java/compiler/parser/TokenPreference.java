package compiler.parser;

public class TokenPreference implements Comparable<TokenPreference> {

	private int preference;

	public TokenPreference(int preference){
		this.preference = preference;
	}
	
	@Override
	public int compareTo(TokenPreference other) {
		return Integer.compare(this.preference , other.preference);
	}

}
