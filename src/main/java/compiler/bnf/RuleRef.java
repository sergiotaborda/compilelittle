/**
 * 
 */
package compiler.bnf;


/**
 * 
 */
public class RuleRef extends BnfAstNode {

	private String name;

	/**
	 * @param id
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	public String toString(){
		return name;
	}
}
