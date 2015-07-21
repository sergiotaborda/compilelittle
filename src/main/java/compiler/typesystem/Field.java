/**
 * 
 */
package compiler.typesystem;

/**
 * 
 */
public class Field {

	private Type type;
	private String name;
	private Type declaringType;

	/**
	 * Constructor.
	 * @param typeAdapter
	 * @param name
	 * @param fromClass
	 */
	public Field(Type declaringType, String name, Type type) {
		this.type = type;
		this.name = name;
		this.declaringType = declaringType;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	

	public Type getDeclaringType() {
		return declaringType;
	}



}
