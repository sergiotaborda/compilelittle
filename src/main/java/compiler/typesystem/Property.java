/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class Property {

	private TypeVariable type;
	private String name;
	private TypeDefinition declaringType;
	private boolean isFinal;
	 
	/**
	 * Constructor.
	 * @param typeAdapter
	 * @param name
	 * @param fromClass
	 */
	public Property(TypeDefinition declaringType, String name, TypeVariable type, boolean isFinal ) {
		this.type = type;
		this.name = name;
		this.declaringType = declaringType;
		this.isFinal = isFinal;
	}

	public TypeVariable getReturningType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public TypeDefinition getDeclaringType() {
		return declaringType;
	}

	public boolean isFinal() {
		return isFinal;
	}



}
