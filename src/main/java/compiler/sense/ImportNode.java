/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class ImportNode extends SenseAstNode {

	
	private QualifiedNameNode name;

	public QualifiedNameNode getName() {
		return name;
	}

	public void setName(QualifiedNameNode name) {
		this.name = name;
	}
}
