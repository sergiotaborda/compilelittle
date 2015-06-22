/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class FieldAccessNode extends NeedTypeCalculationNode {

	private String name;

	/**
	 * @param string
	 */
	public void setName(String name) {
		this.name= name;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

}
