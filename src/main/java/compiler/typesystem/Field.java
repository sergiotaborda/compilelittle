/**
 * 
 */
package compiler.typesystem;


/**
 * 
 */
public class Field implements TypeMember {

	private TypeDefinition type;
	private String name;
	private TypeDefinition declaringType;
	private boolean isFinal;
	 
	/**
	 * Constructor.
	 * @param typeAdapter
	 * @param name
	 * @param fromClass
	 */
	public Field(String name, TypeDefinition type, boolean isFinal) {
		this.type = type;
		this.name = name;
		this.isFinal = isFinal;
	}

	public TypeDefinition getReturningType() {
		return type;
	}

	public String getName() {
		return name;
	}

	

	public TypeDefinition getDeclaringType() {
		return declaringType;
	}

	public boolean isFinal(){
		return isFinal;
	}

	/**
	 * @param senseType
	 * @return
	 */
//	public Field replicate(TypeDefinition newDeclaringType) {
//		return new Field(newDeclaringType, name, type, imutabilityValue);
//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isField() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isProperty() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMethod() {
		return false;
	}

	/**
	 * @param senseTypeDefinition
	 */
	public void setDeclaringType(TypeDefinition declaringType) {
		this.declaringType = declaringType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeMember changeDeclaringType(TypeDefinition concrete) {
		Field f = new Field(this.name, this.type, this.isFinal);
		f.setDeclaringType(concrete);
		return f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConstructor() {
		return false;
	}



}
