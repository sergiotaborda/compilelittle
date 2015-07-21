/**
 * 
 */
package compiler.sense.typesystem;

import java.util.Arrays;
import java.util.List;

import compiler.typesystem.Type;

/**
 * 
 */
public class Method {

	private final String name;
	private boolean isStatic = false;
	private final Type declaringType;
	private final Type returningType;
	private final List<MethodParameter> parameters;
	
	public Method(Type declaringType , String name, Type returningType, MethodParameter ... parameters){
		this.declaringType = declaringType; 
		this.name = name;
		this.returningType = returningType;
		this.parameters = Arrays.asList(parameters);
	}
	
	
	public String toString(){
		return declaringType.getName() + "." + name;
	}
	
	/**
	 * Obtains {@link boolean}.
	 * @return the isStatic
	 */
	public boolean isStatic() {
		return isStatic;
	}


	/**
	 * Atributes {@link boolean}.
	 * @param isStatic the isStatic to set
	 */
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}


	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	public Type getReturningType(){
		return returningType;
	}

	public Type getDeclaringType(){
		return declaringType;
	}
	
	/**
	 * @return
	 */
	public List<MethodParameter> getParameters() {
		return parameters;
	}

}
