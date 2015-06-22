/**
 * 
 */
package compiler.sense.typesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import compiler.sense.GenericType;
import compiler.sense.SenseTypeResolver;
import compiler.sense.typesystem.TypeParameter.Variance;

/**
 * 
 */
public class Type {

	public static final Type Any = new Type("sense.Any");
	public static final Type Exception = new Type("sense.Exception", Any);
	public static final Type Void = new Type("sense.Void", Any);
	public static final Type Null = new Type("sense.Null", Any);
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
	public static final Type Progression =  new Type("sense.Progression", Sequence);;	
	
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
		 Double.addMethod("remainder", Double, new MethodParameter(Double) );
		 
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
	
	public String getName(){
		return name;
	}
	protected boolean isGeneric(){
		return false;
	}
	
	public TypeMatch matchAssignableTo (Type other){
		if (this.equals(other)){
			return TypeMatch.Exact;
		} else if (this.isAssignableTo(other)){
			return TypeMatch.UpCast;
		} else if (this.isPromotableTo(other)){
			return TypeMatch.Promote;
		} else {
			return TypeMatch.NoMatch;
		}
	}
	
	/**
	 * @return
	 */
	public Type getSuperType() {
		return superType;
	}
	
	/**
	 * @param string2
	 * @return
	 */
	public Type addMethod(java.lang.String name, Type returningType, MethodParameter ... parameters) {
		
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

	public Type of(Type parameterType){
		Type dev = new Type(this.name, superType);
		dev.addParameter(parameterType);
		return dev;
	}
	
	/**
	 * @param g
	 */
	private void addParameter(Type g) {
		parameters.add(new TypeParameter(g, Variance.Invariant));
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
		
		if (other.isGeneric()){
			return isAssignableTo(((GenericType)other).getSuperType());
		}
		
		if (this.name.equals( other.name) && other.parameters.size() == 0 ){
			return true;
		} else if( this.name.equals( other.name) 
				&& this.parameters.size() == other.parameters.size()){
			
			for(int i =0; i < this.parameters.size();i++){
				// TODO consider variance
				if (!this.parameters.get(i).getType().equals(other.parameters.get(i).getType())){
					return false;
				}
			}
			
			return true;
		}
		
		return (this.superType != null && this.superType.isAssignableTo(other));
	}


	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof Type && this.equals(((Type)other));
	}
	
	public boolean equals(Type other){
		if( this.name.equals( other.name) 
				&& this.parameters.size() == other.parameters.size()){
			
			for(int i =0; i < this.parameters.size();i++){
				if (!this.parameters.get(i).equals(other.parameters.get(i))){
					return false;
				}
			}
			return true;
		}
		return false;

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
	
	public Optional<Method> getAppropriateMethod(MethodSignature aparentSignature) {
		List<Method> list =getAvailableMethods(aparentSignature.getName()).stream()
				.filter(md -> md.getParameters().size() == aparentSignature.getParameters().size()).collect(Collectors.toList());
		
		if (list.size() > 1){
			
			Method topCandidate = null;
			int score = 0;
			for( Method m : list){
				
				int sum = 0;
				for (int p  = 0; p <  aparentSignature.getParameters().size(); p++){
					Type real = m.getParameters().get(p).getType();
					Type candidate = aparentSignature.getParameters().get(p).getType();
					
					sum += TypeMatch.NoMatch.ordinal() - candidate.matchAssignableTo(real).ordinal();
				}
				
				if (sum > score){
					topCandidate = m;
				}
			}
			return Optional.of(topCandidate);
		} else if(list.isEmpty()){
			return Optional.empty();
		} else {
			return Optional.of(list.get(0));
		}

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
	 * @return
	 */
	public boolean isPrimitive() {
		return false;
	}

}
