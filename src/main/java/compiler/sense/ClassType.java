/**
 * 
 */
package compiler.sense;

import compiler.syntax.AstNode;

/**
 * 
 */
public class ClassType extends AstNode {

	private String name;
	private ClassBody body;
	private QualifiedName upperType;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ClassBody getBody() {
		return body;
	}

	public void setBody(ClassBody body) {
		this.body = body;
		this.add(body);
	}

	public QualifiedName getUpperType() {
		return upperType;
	}

	public void setUpperType(QualifiedName upperType) {
		this.upperType = upperType;
	}



}
