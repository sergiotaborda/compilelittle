/**
 * 
 */
package compiler.sense.typesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import compiler.sense.SenseTypeResolver;
import compiler.sense.typesystem.TypeParameter.Variance;

/**
 * 
 */
public class Type {

	public static final Type Any = new Type("sense.Any");
	public static final Type Exception = new Type("sense.Exception", Any);
	public static final Type Void = new Type("sense.Void", Any);
	public static final Type Boolean = new Type("sense.Boolean", Any);
	public static final Type Number = new Type("sense.Number", Any);
	public static final Type Whole = new Type("sense.Whole", Number);
	public static final Type Int = new Type("sense.Int", Whole);
	public static final Type Natural = new Type("sense.Natural", Whole).addMethod("toInt", Int);

	public static final Type String = new Type("sense.String", Any);
	
	public static final Type Long = new Type("sense.Long",Whole);
	
	public static final Type Complex = new Type("sense.Complex", Number);
	public static final Type Imaginary = new Type("sense.Imaginary", Complex);
	
	public static final Type Real = new Type("sense.Real", Complex);

	public static final Type Decimal = new Type("sense.Decimal",Real);
	
	public static final Type FloatingPointReal = new Type("sense.FloatingPointReal", Real);
	
	public static final Type Double = new Type("sense.Double", FloatingPointReal);
	public static final Type Float = new Type("sense.Float", FloatingPointReal).addMethod("toDouble", Double);
	
	public static final Type Rational = new Type("sense.Rational", Real);

	
	public static final Type Iterable = new Type("sense.Iterable", Any);
	public static final Type Sequence = new Type("sense.Sequence", Iterable);	
	public static final Type Array = new Type("sense.Array", Sequence);	
	
	static {
		 String.addMethod("get", String, new MethodParameter(Natural) );

		 Natural.addMethod("multiply", Int, new MethodParameter(Int) );
		 Natural.addMethod("multiply", Long, new MethodParameter(Long) );
		 Natural.addMethod("toLong", Long );
		 Natural.addMethod("toDecimal", Decimal );
		 Natural.addMethod("toReal", Real );
		 Natural.addMethod("toFloat", Float );
		 Natural.addMethod("negative", Int );
		 
		 Long.addMethod("remainder", Long, new MethodParameter(Int) );
		 
		 Int.addMethod("plus", Long, new MethodParameter(Long) );
		 
	
	}
	
	private String name;
	private String simpleName;
	private Type superType;
	private List<Method> methods = new ArrayList<>();
	private List<TypeParameter> parameters  = new ArrayList<>(0);

	/**
	 * Constructor.
	 * @param string
	 */
	public Type(String name) {
		this.name = name;
		if (name.contains(".")){
			String[] s = name.split("\\.");
			simpleName = s[s.length - 1];
		} else {
			simpleName = name;
		}
		SenseTypeResolver.getInstance().registerType(name, this);
	}
	
	/**
	 * @param string2
	 * @return
	 */
	private Type addMethod(java.lang.String name, Type returningType, MethodParameter ... parameters) {
		
		this.methods.add(new Method(this, name , returningType, parameters));
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
		if ( parameters.isEmpty() ){
			return name;
		}
		
		StringBuilder builder = new StringBuilder(name).append("<");
		
		for (TypeParameter p : parameters){
			builder.append(p.getType().toString()).append(",");
		}
		builder.deleteCharAt(builder.length()- 1);
		return builder.append(">").toString();
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

	/**
	 * @param type
	 * @return
	 */
	public boolean isPromotableTo(Type type) {
		return getAvailableMethods("to" + type.getSimpleName()).size() > 0;
	}

	/**
	 * @return
	 */
	public java.lang.String getSimpleName() {
		return simpleName;
	}

	public List<TypeParameter> getParameters(){
		return parameters;
	}
	/**
	 * @param g
	 */
	public void addParameter(Type g) {
		parameters.add(new TypeParameter(g, Variance.Invariant));
	}

}
