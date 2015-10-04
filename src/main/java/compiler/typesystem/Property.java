/**
 * 
 */
package compiler.typesystem;

import compiler.sense.ast.Imutability;

/**
 * 
 */
public class Property {

	private TypeVariable type;
	private String name;
	private TypeDefinition declaringType;
	private Imutability imutabilityValue;
	 
	/**
	 * Constructor.
	 * @param typeAdapter
	 * @param name
	 * @param fromClass
	 */
	public Property(TypeDefinition declaringType, String name, TypeVariable type,Imutability imutabilityValue) {
		this.type = type;
		this.name = name;
		this.declaringType = declaringType;
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

	public Imutability getImutabilityValue() {
		return imutabilityValue;
	}



}
