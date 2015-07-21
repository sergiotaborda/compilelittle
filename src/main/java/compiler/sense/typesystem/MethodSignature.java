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
public class MethodSignature {

	private final String name;
	private boolean isStatic = false;
	private final Type declaringType;
	private final List<ConcreteMethodParameter> parameters;
	
	public MethodSignature(Type declaringType , String name, ConcreteMethodParameter ... parameters){
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
	public List<ConcreteMethodParameter> getParameters() {
		return parameters;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean equals(Object other){
		return other instanceof MethodSignature && equals((MethodSignature)other);
	}
	
	private boolean equals(MethodSignature other){
		if( this.name.equals(other.name) && this.parameters.size() == other.parameters.size()){
			
			for (int i = 0; i < parameters.size(); i++ ){
				if (!this.parameters.get(i).getType().equals(other.parameters.get(i).getType())){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean isAssignableTo(MethodSignature other){
		if ( this.name.equals(other.name) && this.parameters.size() == other.parameters.size()){
			
			for (int i = 0; i < parameters.size(); i++ ){
				if (!this.parameters.get(i).getType().isAssignableTo(other.parameters.get(i).getType())){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public int hashCode (){
		return name.hashCode() + 31 * parameters.size();
	}

}
