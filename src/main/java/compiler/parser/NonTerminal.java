/**
 * 
 */
package compiler.parser;



/**
 * 
 */
public class NonTerminal extends AbstractProduction   {

	private String name;
	private Production rule;

	/**
	 * Constructor.
	 * @param name
	 */
	public NonTerminal(String name) {
		this.name = name;
	}

	public NonTerminal addSemanticAction(SemanticAction action)
	{
		return (NonTerminal)super.addSemanticAction(action);
	}
	
	/**
	 * @param string
	 * @return
	 */
	public static NonTerminal of(String name) {
		return new NonTerminal(name);
	}

	public Production getRule (){
		return rule;
	}

	public void setRule (Production rule){
		if (rule.isNonTerminal()){
			this.rule = new ProductionSequence(rule);
		} else {
			this.rule = rule;
		}
	}

	public String toString(){
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNonTerminal() {
		return true;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return this.label == null ? this.name : this.label;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof NonTerminal) && equalsNonTerminal((NonTerminal)obj); 
	}


	private boolean equalsNonTerminal(NonTerminal other) {
		return this.name == null ? other.name == null : this.name.equals(other.name);
	}

	public int hashCode (){
		return this.name == null ? 0 : this.name.hashCode();
	}

	/**
	 * @return
	 */
	boolean isText(){
		return false;
	}

	/**
	 * @return
	 */
	boolean isIdentifier(){
		return false;
	}

	/**
	 * @return
	 */
	boolean isNumeric(){
		return false;
	}

	/**
	 * @return
	 */
	public boolean isLeftRecursive() {
		return isLeftRecursive(this, rule);
	
	}

	/**
	 * @param nonTerminal
	 * @param rule2
	 * @return
	 */
	private boolean isLeftRecursive(NonTerminal n, Production rule) {
		if (rule.isSequence()){
			return rule.toSequence().get(0) == n;
		} else if (rule.isAlternative()){
			for (Production p : rule.toAlternative()){
				if (isLeftRecursive(n,p)){
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}




}
