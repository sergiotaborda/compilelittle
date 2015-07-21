/**
 * 
 */
package compiler.sense.typesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import compiler.sense.Kind;
import compiler.sense.SenseTypeResolver;
import compiler.sense.typesystem.TypeParameter.Variance;
import compiler.typesystem.Field;
import compiler.typesystem.Type;

/**
 * 
 */
public class SenseType implements Type {

	public static final SenseType Any = new SenseType("sense.Any");
	public static final SenseType Exception = new SenseType("sense.Exception", Any);
	public static final Type Void = new SenseType("sense.Void", Any);
	public static final SenseType Null = new SenseType("sense.Null", Any);
	public static final SenseType Boolean = new SenseType("sense.Boolean", Any);
	public static final SenseType Number = new SenseType("sense.Number", Any);
	public static final SenseType Whole = new SenseType("sense.Whole", Number);
	public static final SenseType Int = new SenseType("sense.Int", Whole);
	public static final SenseType Natural = new SenseType("sense.Natural", Whole).addMethod("toInt", Int);

	public static final SenseType String = new SenseType("sense.String", Any);
	
	public static final SenseType Long = new SenseType("sense.Long",Whole);
	
	public static final SenseType Complex = new SenseType("sense.Complex", Number);
	public static final SenseType Imaginary = new SenseType("sense.Imaginary", Complex);
	
	public static final SenseType Real = new SenseType("sense.Real", Complex);

	public static final SenseType Decimal = new SenseType("sense.Decimal",Real);
	
	public static final SenseType FloatingPointReal = new SenseType("sense.FloatingPointReal", Real);
	
	public static final SenseType Double = new SenseType("sense.Double", FloatingPointReal);
	public static final SenseType Float = new SenseType("sense.Float", FloatingPointReal).addMethod("toDouble", Double);
	
	public static final SenseType Rational = new SenseType("sense.Rational", Real);

	
	public static final SenseType Iterable = new SenseType("sense.Iterable", Any, Kind.Interface).withGenericParameter(Variance.Invariant,"T");
	public static final SenseType Sequence = new SenseType("sense.Sequence", Iterable,Kind.Interface).withGenericParameter(Variance.Invariant,"T");	
	public static final SenseType Array = new SenseType("sense.Array", Sequence).withGenericParameter(Variance.Invariant,"T");
	public static final SenseType Progression =  new SenseType("sense.Progression", Sequence,Kind.Interface).withGenericParameter(Variance.Invariant,"T");
	public static final SenseType Character =  new SenseType("sense.Character", Any);
	public static final SenseType Maybe = new SenseType("sense.Maybe", Any).withGenericParameter(Variance.Invariant,"T");
	public static final SenseType Some = new SenseType("sense.Some", Maybe).withGenericParameter(Variance.Invariant,"T");	
	public static final SenseType None = new SenseType("sense.None", Maybe);	
	
	public static final SenseType Interval = new SenseType("sense.Interval", Any).withGenericParameter(Variance.Invariant,"T");
			
	
	public static final Type Nothing = new NothingType();
	
	static {
		 String.addMethod("get", String, new ConcreteMethodParameter(Natural) );

		 Natural.addMethod("multiply", Int, new ConcreteMethodParameter(Int) );
		 Natural.addMethod("multiply", Long, new ConcreteMethodParameter(Long) );
		 Natural.addMethod("toLong", Long );
		 Natural.addMethod("toDecimal", Decimal );
		 Natural.addMethod("toReal", Real );
		 Natural.addMethod("toFloat", Float );
		 Natural.addMethod("negative", Int );
		 
		 Long.addMethod("remainder", Long, new ConcreteMethodParameter(Int) );
		 Double.addMethod("remainder", Double, new ConcreteMethodParameter(Double) );
		 
		 Int.addMethod("plus", Long, new ConcreteMethodParameter(Long) );
		 
		 Interval.addMethod("contains", Boolean, new GenericVariantMethodParameter("candidade",Interval, 0));
	}
	
	private String name;
	private String simpleName;
	private Type superType;
	private List<Method> methods = new ArrayList<>();
	private List<TypeParameter> parameters  = new ArrayList<>(0);
	private Kind kind;

	/**
	 * Constructor.
	 * @param string
	 */
	public SenseType(String name) {
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
	 * Constructor.
	 * @param string2
	 * @param number2
	 */
	public SenseType(String name, Type superType) {
		this(name, superType, Kind.Class);
	}

	public SenseType(String name, Type superType, Kind kind) {
		this(name);
		this.superType = superType;
		this.kind = kind;
	}
	
	/**
	 * Constructor.
	 * @param senseType
	 */
	public SenseType(SenseType other) {
		this(other.name,other.superType, other.kind);
		this.methods = other.methods;
		
		
		this.parameters = new ArrayList<TypeParameter>(other.parameters.size());
		
		for(TypeParameter t : other.parameters){
			this.parameters.add(new TypeParameter(t));
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName(){
		return name;
	}


	public boolean isGeneric(){
		return !parameters.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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
	 * {@inheritDoc}
	 */
	@Override
	public Type getSuperType() {
		return superType;
	}
	
	/**
	 * @param string2
	 * @return
	 */
	public SenseType addMethod(java.lang.String name, SenseType returningType, MethodParameter ... parameters) {
		
		this.methods.add(new Method(this, name , returningType, parameters));
		return this;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public SenseType of(Type ... parameterType){
		if (!this.isGeneric()){
			throw new IllegalStateException("Type " + this.getName() + " is not generic");
		}
		
		if (this.parameters.size() != parameterType.length){
			throw new IllegalStateException("Type " + this.getName() + " has " + this.parameters.size() + " generic types, not " +  parameterType.length);
		}
		
		SenseType dev = new SenseType(this);
		List<TypeParameter> params = dev.getParameters();
		for (int i =0; i < parameterType.length; i++){
			params.get(i).setConcreteType(parameterType[i]);
		}
		
		return dev;
	}
	
	public SenseType withGenericParameter(Variance variance, String parameterTypeName){
		SenseType dev = new SenseType(this);
		dev.addParameter(variance, parameterTypeName, SenseType.Any, SenseType.Nothing);
		return dev;
	}
	
	/**
	 * @param g
	 */
	private void addParameter(Variance variance, String parameterTypeName, Type upperBound, Type lowerBound) {
		parameters.add(new TypeParameter(variance,parameterTypeName, upperBound, lowerBound ));
	}
	
	public String toString(){
		if ( parameters.isEmpty() ){
			return name;
		}
		
		StringBuilder builder = new StringBuilder(name).append("<");
		
		for (TypeParameter p : parameters){
			builder.append(p.getName().toString());
			if(!p.getUpperbound().equals(SenseType.Any)){
				builder.append(" extends ").append(p.getUpperbound().getSimpleName());
			}
			if(p.getLowerBound() != null && !p.getLowerBound().equals(SenseType.Nothing)){
				builder.append(" super ").append(p.getLowerBound().getSimpleName());
			}
			builder.append(",");
		}
		builder.deleteCharAt(builder.length()- 1);
		return builder.append(">").toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SenseType or(Type other) {
		return new UnionType(this, other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAssignableTo(Type type) {
		
		if (type.isGeneric()){
			return isAssignableTo(type.getSuperType());
		}
		
		if (type instanceof SenseType){
			SenseType other = (SenseType)type;
			
			if (this.name.equals( other.name) && other.parameters.size() == 0 ){
				return true;
			} else if( this.name.equals( other.name) 
					&& this.parameters.size() == other.parameters.size()){
				
				for(int i =0; i < this.parameters.size();i++){
					// TODO consider variance and lowerbound
					if (!this.parameters.get(i).getUpperbound().equals(other.parameters.get(i).getUpperbound())){
						return false;
					}
				}
				
				return true;
			}
		}
		
		
		return isPromotableTo(type) || ((this.superType != null && this.superType.isAssignableTo(type)));
	}


	public int hashCode(){
		return name.hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof SenseType && this.equals(((SenseType)other));
	}
	
	public boolean equals(SenseType other){
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Method> getDeclaredMethods(String name) {
		return methods.stream().filter(m -> m.getName().equals(name)).collect(Collectors.toList());
	}
	 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Method> getAvailableMethods(String name) {
		List<Method> list = getDeclaredMethods(name);
		if (list.isEmpty() && superType != null){
			return superType.getAvailableMethods(name);
		}
		return list;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPromotableTo(Type type) {
		return getAvailableMethods("to" + type.getSimpleName()).size() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String getSimpleName() {
		return simpleName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TypeParameter> getParameters(){
		return parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPrimitive() {
		return false;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Kind getKind() {
		return kind;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Field> getAvailableFields(java.lang.String fieldName) {
		throw new UnsupportedOperationException("Not implememented yet");
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Field> getDeclaredFields(java.lang.String name) {
		throw new UnsupportedOperationException("Not implememented yet");
	}
	

}
