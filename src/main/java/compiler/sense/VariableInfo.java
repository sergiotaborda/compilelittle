/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;

/**
 * 
 */
public class VariableInfo {

	private Type type;
	private String name;
	private boolean initialized;

	/**
	 * Constructor.
	 * @param name
	 * @param type
	 */
	public VariableInfo(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Obtains {@link Type}.
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Obtains {@link String}.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param b
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public boolean isInitialized(){
		return initialized;
	}

	
}
