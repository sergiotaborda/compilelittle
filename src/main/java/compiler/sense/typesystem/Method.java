/**
 * 
 */
package compiler.sense.typesystem;

/**
 * 
 */
public class Method {

	private String name;
	private Type declaringType;
	private Type returningType;

	public Method(Type declaringType , String name, Type returningType){
		this.declaringType = declaringType; 
		this.name = name;
		this.returningType = returningType;
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

}
