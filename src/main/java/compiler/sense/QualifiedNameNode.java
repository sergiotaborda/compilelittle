/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class QualifiedNameNode extends SenseAstNode {

	private StringBuilder name = new StringBuilder();

	
	public QualifiedNameNode (){}
	public QualifiedNameNode (String name){
		this.name= new StringBuilder(name);
	}
	
	public String getName() {
		return name.toString();
	}

	public void setName(String name) {
		this.name = new StringBuilder(name);
	}

	/**
	 * @param string
	 */
	public void append(String s) {
		if (name.length() > 0){
			name.append(".");
		}
		name.append(s);
	}
	
	public String toString(){
		return name.toString();
	}
	/**
	 * @return
	 */
	public String getPrevious() {
		int pos = name.lastIndexOf(".");
		if (pos < 0){
			return "";
		} else {
			return name.subSequence(0, pos).toString();
		}
	}
}
