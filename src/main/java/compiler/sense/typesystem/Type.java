/**
 * 
 */
package compiler.sense.typesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import compiler.sense.SenseTypeResolver;

/**
 * 
 */
public class Type {

	public static final Type Any = new Type("sense.Any");
	public static final Type Void = new Type("sense.Void", Any);
	public static final Type String = new Type("sense.String", Any);
	public static final Type Boolean = new Type("sense.Boolean", Any);
	public static final Type Number = new Type("sense.Number", Any);
	public static final Type Whole = new Type("sense.Whole", Number);
	public static final Type Natural = new Type("sense.Natural", Whole);
	public static final Type Int = new Type("sense.Int", Whole);
	public static final Type Long = new Type("sense.Long",Whole);
	
	public static final Type Complex = new Type("sense.Complex", Number);
	public static final Type Imaginary = new Type("sense.Imaginary", Complex);
	
	public static final Type Real = new Type("sense.Real", Complex);

	public static final Type Decimal = new Type("sense.Decimal",Real);
	
	public static final Type FloatingPointReal = new Type("sense.FloatingPointReal", Real);
	
	public static final Type Double = new Type("sense.Double", FloatingPointReal);
	public static final Type Float = new Type("sense.Float", FloatingPointReal).addMethod("toDouble", Double);
	
	public static final Type Rational = new Type("sense.Rational", Real);
	
	
	
			
	private String name;
	private Type superType;
	private List<Method> methods = new ArrayList<>();

	/**
	 * Constructor.
	 * @param string
	 */
	public Type(String name) {
		this.name = name;
		SenseTypeResolver.getInstance().registerType(name, this);
	}
	
	/**
	 * @param string2
	 * @return
	 */
	private Type addMethod(java.lang.String name, Type returningType) {
		
		this.methods.add(new Method(this, name , returningType));
		return this;
	}

	/**
	 * Constructor.
	 * @param string2
	 * @param number2
	 */
	public Type(String name, Type superType) {
		this(name);
		this.superType = superType;
	}

	public String toString(){
		return name;
	}

	/**
	 * @param type
	 * @return
	 */
	public Type or(Type other) {
		return new UnionType(this, other);
	}

	/**
	 * @param type
	 * @return
	 */
	public boolean isAssignableTo(Type other) {
		return this.equals(other) || (this.superType != null && this.superType.isAssignableTo(other));
	}


	public List<Method> getDeclaredMethods(String name) {
		return methods.stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
	}
	 
	/**
	 * @param name2
	 */
	public List<Method> getAvailableMethods(String name) {
		List<Method> list = getDeclaredMethods(name);
		if (list.isEmpty() && superType != null){
			return superType.getAvailableMethods(name);
		}
		return list;
	}

}
