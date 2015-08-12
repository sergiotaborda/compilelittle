/**
 * 
 */
package compiler.sense.ast;


/**
 * 
 */
public class AnnotationNode extends SenseAstNode {

	private String name;

	/**
	 * @param string
	 */
	public void setName(String name) {
		this.name= name;
	}
	
	public String getName(){
		return name;
	}

}
