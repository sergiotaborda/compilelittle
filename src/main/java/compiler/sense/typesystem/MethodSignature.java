/**
 * 
 */
package compiler.sense.typesystem;

import java.util.Arrays;
import java.util.List;

/**
 * 
 */
public class MethodSignature {

	private final String name;
	private boolean isStatic = false;
	private final Type declaringType;
	private final List<MethodParameter> parameters;
	
	public MethodSignature(Type declaringType , String name, MethodParameter ... parameters){
		this.declaringType = declaringType; 
		this.name = name;
		this.parameters = Arrays.asList(parameters);
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

	public Type getDeclaringType(){
		return declaringType;
	}
	
	/**
	 * @return
	 */
	public List<MethodParameter> getParameters() {
		return parameters;
	}
	
	public String getName() {
		return name;
	}

}
