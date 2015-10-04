/**
 * 
 */
package compiler.typesystem;

import java.util.Arrays;
import java.util.List;

import compiler.sense.typesystem.SenseTypeSystem;

/**
 * 
 */
public class MethodSignature {

	private final String name;
	private boolean isStatic = false;
	private final List<MethodParameter> parameters;
	
	public MethodSignature(String name, MethodParameter ... parameters){
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

	
	/**
	 * @return
	 */
	public List<MethodParameter> getParameters() {
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
	
	public int hashCode (){
		return name.hashCode() + 31 * parameters.size();
	}


	
	public boolean isAssignableTo(MethodSignature other){
		if ( this.name.equals(other.name) && this.parameters.size() == other.parameters.size()){
			
			for (int i = 0; i < parameters.size(); i++ ){
				if (!SenseTypeSystem.getInstance().isAssignableTo(this.parameters.get(i).getType().getUpperbound(), other.parameters.get(i).getType().getUpperbound())){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	

	/**
	 * @param s
	 * @return
	 */
	public boolean isPromotableTo(MethodSignature other) {
		if ( this.name.equals(other.name) && this.parameters.size() == other.parameters.size()){
			
			for (int i = 0; i < parameters.size(); i++ ){
				if (!SenseTypeSystem.getInstance().isPromotableTo(this.parameters.get(i).getType().getUpperbound(), other.parameters.get(i).getType().getUpperbound())){
					return false;
				}
			}
			return true;
		}
		return false;
	}




	/**
	 * @param m
	 * @return
	 */
	public boolean isImplementedBy(Method method) {
		if (this.name.equals(method.getName()) &&  this.parameters.size() == method.getParameters().size()){
			
			for (int i = 0; i < parameters.size(); i++ ){
				if (!SenseTypeSystem.getInstance().isAssignableTo(this.parameters.get(i).getType().getUpperbound(), method.getParameters().get(i).getType().getUpperbound())){
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
