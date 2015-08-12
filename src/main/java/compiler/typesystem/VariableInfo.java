/**
 * 
 */
package compiler.typesystem;

import compiler.sense.typesystem.SenseType;

/**
 * 
 */
public class VariableInfo {

	private Type type;
	private String name;
	private boolean initialized;
	private boolean isTypeVariable;
	private boolean escapes = false;
	private int writeCount = 0;
	private boolean imutable;

	/**
	 * Constructor.
	 * @param name
	 * @param type
	 */
	public VariableInfo(String name, Type type, boolean isTypeVariable) {
		this.name = name;
		this.type = type;
		this.isTypeVariable= isTypeVariable;
		this.imutable = false;
	}

	public void markEscapes(){
		escapes = true;
	}
	
	public boolean doesEscape(){
		return escapes;
	}
	
	public void markWrite(){
		writeCount++;
	}

	public boolean isEfectivlyFinal(){
		return writeCount < 2;
	}
	/**
	 * Obtains {@link SenseType}.
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


	/**
	 * @return
	 */
	public boolean isTypeVariable() {
		return isTypeVariable;
	}

	public boolean isImutable() {
		return imutable;
	}

	public void setImutable(boolean imutable) {
		this.imutable = imutable;
	}

	
}
