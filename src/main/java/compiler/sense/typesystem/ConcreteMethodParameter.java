/**
 * 
 */
package compiler.sense.typesystem;

import compiler.typesystem.Type;

/**
 * 
 */
public class ConcreteMethodParameter implements MethodParameter {

	private String name;
	private Type type;
	/**
	 * Constructor.
	 * @param natural
	 */
	public ConcreteMethodParameter(Type type) {
		if (type == null){
			throw new IllegalArgumentException("Type is required");
		}
		this.type = type;
	}
	public ConcreteMethodParameter(Type type,String name) {
		if (type == null){
			throw new IllegalArgumentException("Type is required");
		}
		this.type = type;
		this.name = name;
	}
	
	public String toString(){
		return type.getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * {@inheritDoc}
	 */
	@Override
	public Type getType() {
		return type;
	}
	/**
	 * Atributes {@link SenseType}.
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	
	
}
