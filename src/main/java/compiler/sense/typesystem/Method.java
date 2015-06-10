/**
 * 
 */
package compiler.sense.typesystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 */
public class Method {

	private final String name;
	private final Type declaringType;
	private final Type returningType;
	private final List<MethodParameter> parameters;
	
	public Method(Type declaringType , String name, Type returningType, MethodParameter ... parameters){
		this.declaringType = declaringType; 
		this.name = name;
		this.returningType = returningType;
		this.parameters = Arrays.asList(parameters);
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
