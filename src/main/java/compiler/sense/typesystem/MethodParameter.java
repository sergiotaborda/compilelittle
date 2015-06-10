/**
 * 
 */
package compiler.sense.typesystem;

/**
 * 
 */
public class MethodParameter {

	private String name;
	private Type type;
	/**
	 * Constructor.
	 * @param natural
	 */
	public MethodParameter(Type type) {
		this.type = type;
	}
	/**
	 * Obtains {@link string}.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Atributes {@link string}.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Obtains {@link Type}.
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	/**
	 * Atributes {@link Type}.
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	
	
}
