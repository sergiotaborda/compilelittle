/**
 * 
 */
package compiler.typesystem;

import java.util.List;

/**
 * 
 */
public class Constructor implements CallableMember{

	private List<MethodParameter> parameters;
	private TypeDefinition declaringType;

	/**
	 * Constructor.
	 * @param parameters
	 */
	public Constructor(List<MethodParameter> parameters) {
		this.parameters  = parameters;
	}

	
	public List<MethodParameter> getParameters(){
		return parameters;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isField() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "";
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
	 * {@inheritDoc}
	 */
	@Override
	public TypeMember changeDeclaringType(TypeDefinition concrete) {
		Constructor c = new Constructor(this.parameters);
		c.declaringType = concrete;
		return c;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getDeclaringType() {
		return declaringType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSynthetic() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isConstructor() {
		return true;
	}

	/**
	 * @param lenseTypeDefinition
	 */
	public void setDeclaringType(TypeDefinition  declaringType) {
		this.declaringType = declaringType;
	}

	

}
