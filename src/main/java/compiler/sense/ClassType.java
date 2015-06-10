/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class ClassType extends SenseAstNode {

	private String name;
	private ClassBody body;
	private TypeNode superType;
	
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

	public void setSuperType(TypeNode upperType) {
		this.superType = upperType;
		this.add(upperType);
	}

	/**
	 * Obtains {@link TypeNode}.
	 * @return the superType
	 */
	public TypeNode getSuperType() {
		return superType;
	}



}
