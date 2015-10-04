/**
 * 
 */
package compiler.sense.typesystem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import compiler.CompilationError;
import compiler.sense.ast.Imutability;
import compiler.typesystem.MethodParameter;
import compiler.typesystem.MethodReturn;
import compiler.typesystem.TypeDefinition;
import compiler.typesystem.Variance;

/**
 * 
 */
public class SenseTypeSystem {

	private static SenseTypeSystem me = new SenseTypeSystem();

	public static SenseTypeSystem getInstance(){
		return me;
	}

	public static TypeDefinition Any() {
		return getInstance().getForName("sense.lang.Any").get();
	}

	public static TypeDefinition Nothing(){
		return getInstance().getForName("sense.lang.Nothing").get();
	}

	public static TypeDefinition None(){
		return getInstance().getForName("sense.lang.None").get();
	}

	public static TypeDefinition Maybe(){
		return getInstance().getForName("sense.lang.Maybe", 1).get();
	}

	public static TypeDefinition Progression() {
		return getInstance().getForName("sense.lang.Progression",1).get();
	}

	public static TypeDefinition Boolean() {
		return getInstance().getForName("sense.lang.Boolean").get();
	}

	public static TypeDefinition Void() {
		return getInstance().getForName("sense.lang.Void").get();
	}

	public static TypeDefinition Iterable() {
		return getInstance().getForName("sense.lang.Iterable",1).get();
	}

	public static TypeDefinition Exception() {
		return getInstance().getForName("sense.lang.Exception").get();
	}

	public static TypeDefinition Character() {
		return getInstance().getForName("sense.lang.Character").get();
	}

	public static TypeDefinition String() {
		return getInstance().getForName("sense.lang.String").get();
	}

	public static TypeDefinition Natural() {
		return getInstance().getForName("sense.lang.Natural").get();
	}

	public static TypeDefinition Decimal() {
		return getInstance().getForName("sense.lang.Decimal").get();
	}

	public static TypeDefinition Double() {
		return getInstance().getForName("sense.lang.Double").get();
	}

	/**
	 * @return
	 */
	public static TypeDefinition Float() {
		return getInstance().getForName("sense.lang.Float").get();
	}

	/**
	 * @return
	 */
	public static TypeDefinition Imaginary() {
		return getInstance().getForName("sense.lang.Imaginary").get();
	}

	/**
	 * @return
	 */
	public static TypeDefinition Short() {
		return getInstance().getForName("sense.lang.Short").get();
	}

	/**
	 * @return
	 */
	public static TypeDefinition Int() {
		return getInstance().getForName("sense.lang.Int").get();
	}

	/**
	 * @return
	 */
	public static TypeDefinition Long() {
		return getInstance().getForName("sense.lang.Long").get();
	}

	/**
	 * @return
	 */
	public static TypeDefinition Function(int count) {
		return getInstance().getForName("sense.lang.Function", count).get();
	}

	/**
	 * @return
	 */
	public static TypeDefinition Whole() {
		return getInstance().getForName("sense.lang.Whole").get();
	}


	public SenseTypeSystem (){
		SenseTypeDefinition nothing = register(new SenseTypeDefinition("sense.lang.Nothing", Kind.Class, null));

		SenseTypeDefinition any = register(new SenseTypeDefinition("sense.lang.Any", Kind.Class, null));
	    register(new SenseTypeDefinition("sense.lang.Exception", Kind.Class, any));

		SenseTypeDefinition function2 = register(new SenseTypeDefinition("sense.lang.Function", Kind.Class, any,
				new GenericDefinition("R", Variance.Invariant, any, nothing),
				new GenericDefinition("T", Variance.Invariant, any, nothing)
		));
		
		SenseTypeDefinition function3 = register(new SenseTypeDefinition("sense.lang.Function", Kind.Class, any,
				new GenericDefinition("R", Variance.Invariant, any, nothing),
				new GenericDefinition("T", Variance.Invariant, any, nothing),
				new GenericDefinition("T", Variance.Invariant, any, nothing)
		));
		
		// TODO treat interfaces and traits as attached types
		SenseTypeDefinition iterable = register(new SenseTypeDefinition("sense.lang.Iterable", Kind.Interface, any, new GenericDefinition("T", Variance.Covariant, any,nothing))); 
		SenseTypeDefinition maybe = register(new SenseTypeDefinition("sense.lang.Maybe", Kind.Class, any, new GenericDefinition("T", Variance.Covariant, any,nothing)));  

		maybe.addMethod("map", specify(maybe, any), new MethodParameter(function2)); // TODO return must obey function return
		
		SenseTypeDefinition svoid = register(new SenseTypeDefinition("sense.lang.Void", Kind.Class, any));

		SenseTypeDefinition none = register(new SenseTypeDefinition("sense.lang.None", Kind.Class, specify(maybe, nothing), new GenericTypeParameter[0]));
		
		none.addField("None", none, Imutability.Imutable); // TODO this a Fake static field 
		
		register(new SenseTypeDefinition("sense.lang.Some", Kind.Class, maybe));

		SenseTypeDefinition sbool = register(new SenseTypeDefinition("sense.lang.Boolean", Kind.Class, any));
		SenseTypeDefinition character = register(new SenseTypeDefinition("sense.lang.Character", Kind.Class, any));
		register(new SenseTypeDefinition("sense.lang.Exception", Kind.Class, any));

		register(new SenseTypeDefinition("sense.lang.Progression", Kind.Class, iterable));
		SenseTypeDefinition sequence = register(new SenseTypeDefinition("sense.lang.Sequence", Kind.Class, iterable));
		SenseTypeDefinition array = register(new SenseTypeDefinition("sense.lang.Array", Kind.Class, sequence));

		SenseTypeDefinition number = register(new SenseTypeDefinition("sense.lang.Number", Kind.Class, any));
		SenseTypeDefinition whole = register(new SenseTypeDefinition("sense.lang.Whole", Kind.Class, number));
		SenseTypeDefinition natural =register(new SenseTypeDefinition("sense.lang.Natural", Kind.Class, whole));

		SenseTypeDefinition integer = register(new SenseTypeDefinition("sense.lang.Integer", Kind.Class, whole));
		SenseTypeDefinition sint = register(new SenseTypeDefinition("sense.lang.Int", Kind.Class, integer));
		SenseTypeDefinition slong =register(new SenseTypeDefinition("sense.lang.Long", Kind.Class, integer));
		SenseTypeDefinition sshort =register(new SenseTypeDefinition("sense.lang.Short", Kind.Class, integer));
		
		whole.addMethod("toInt", sint);
		whole.addMethod("toLong", slong);
		whole.addMethod("toShort", sshort);
		
		integer.addMethod("toInt", sint);
		integer.addMethod("toLong", slong);
		integer.addMethod("toShort", sshort);
		
		natural.addMethod("multiply", natural, new MethodParameter(natural));
		natural.addMethod("remainder", natural, new MethodParameter(natural));
		natural.addMethod("plus", natural, new MethodParameter(natural));
		
		natural.addMethod("multiply", integer, new MethodParameter(integer));
		natural.addMethod("remainder", integer, new MethodParameter(integer));
		natural.addMethod("plus", integer, new MethodParameter(integer));
		
		
		integer.addMethod("multiply", integer, new MethodParameter(integer));
		integer.addMethod("remainder", integer, new MethodParameter(integer));
		integer.addMethod("plus", integer, new MethodParameter(integer));
		
		natural.addMethod("negative", sint); // TODO could overflow
		
		sequence.addField("size", natural, Imutability.Mutable); // TODO should be Property

		SenseTypeDefinition string = register(new SenseTypeDefinition("sense.lang.String", Kind.Class, specify(sequence, character), new GenericTypeParameter[0]));

		string.addMethod("toMaybe", specify(maybe, string));
		string.addMethod("get", character, new MethodParameter (natural));
		any.addMethod("toString", string);

		SenseTypeDefinition real = register(new SenseTypeDefinition("sense.lang.Real", Kind.Class, number));
		
		SenseTypeDefinition decimal = register(new SenseTypeDefinition("sense.lang.Decimal", Kind.Class, real));
		SenseTypeDefinition sdouble = register(new SenseTypeDefinition("sense.lang.Double", Kind.Class, decimal));
		SenseTypeDefinition sfloat = register(new SenseTypeDefinition("sense.lang.Float", Kind.Class, decimal));

		whole.addMethod("toDouble", sdouble);
		whole.addMethod("toFloat", sfloat);
		whole.addMethod("toDecimal", decimal);
		whole.addMethod("toReal", real);
		real.addMethod("toDouble", sdouble);
		real.addMethod("toFloat", sfloat);
		real.addMethod("toDecimal", decimal);
		
		SenseTypeDefinition img = register(new SenseTypeDefinition("sense.lang.Imaginary", Kind.Class, number));
		SenseTypeDefinition complex = register(new SenseTypeDefinition("sense.lang.Complex", Kind.Class, number));
		
		SenseTypeDefinition interval = register(new SenseTypeDefinition("sense.lang.Interval", Kind.Class, any, new GenericDefinition("T", Variance.Invariant, any, nothing))); // TODO use Comparable
		
		interval.addMethod("contains", sbool, new MethodParameter(any));
		
		whole.addMethod("plus", complex, new MethodParameter(img));
		whole.addMethod("minus", complex, new MethodParameter(img));
		

		
		SenseTypeDefinition math = register(new SenseTypeDefinition("sense.lang.Math", Kind.Class, any));
		
		math.addMethod("sin", sdouble, new MethodParameter(sdouble));
		
		SenseTypeDefinition console = register(new SenseTypeDefinition("sense.lang.Console", Kind.Class, any));
		console.addMethod("println", svoid, new MethodParameter(string));

	}

	private Map<TypeKey, SenseTypeDefinition> definitions = new HashMap<>();

	public SenseTypeDefinition register(SenseTypeDefinition definition){
		definitions.put(new TypeKey(definition.getGenericParameters(), definition.getName()), definition);
		return definition;
	}

	public Optional<SenseTypeDefinition> getForName(String name, GenericTypeParameter ... genericTypeParameters){
		return Optional.ofNullable(definitions.get(new TypeKey(Arrays.asList(genericTypeParameters), name)));
	}

	/**
	 * @param name
	 * @param i
	 * @return
	 */
	public Optional<SenseTypeDefinition> getForName(java.lang.String name, int genericParametersCount) {
		for(TypeKey key : definitions.keySet()){
			if (key.getName().equals(name) && key.getGenericTypeParameters().size() == genericParametersCount){
				return Optional.of(definitions.get(key));
			}
		}
		return Optional.empty();
	}

	public boolean isAssignableTo(GenericTypeParameter type, GenericTypeParameter target){
	    if (target.getVariance() == Variance.ContraVariant){
	    	isAssignableTo(target.getUpperbound(), type.getUpperbound());
		} else if (target.getVariance() == Variance.Covariant){
			return isAssignableTo(type.getUpperbound(), target.getUpperbound());
		}
		 
		return isAssignableTo(type.getUpperbound(), target.getUpperbound()) && isAssignableTo(type.getLowerBound(), target.getLowerBound()) ;
	}
	
	public boolean isAssignableTo(TypeDefinition type, TypeDefinition target){
		if (target.equals(Any())){
			return true;
		}
		if (type.equals(Nothing())){
			return true;
		}
		if (target.equals(Nothing())){
			return false;
		}
		if (type.equals(Any())){
			return false;
		}


		if (type.getName().equals( target.getName()) &&  type.getGenericParameters().size() == target.getGenericParameters().size() ){
			boolean assignable = true;
			for (int i = 0; i < type.getGenericParameters().size(); i++){
				if (!isAssignableTo(type.getGenericParameters().get(i), target.getGenericParameters().get(i))){
					assignable = false;
					break;
				}
			}
			if (assignable){
				return true;
			}
		} 

		if (type.getSuperDefinition() != null){
			if (!type.getSuperDefinition().equals(Any()) && !type.getGenericParameters().isEmpty()){
				TypeDefinition[] types = new TypeDefinition[type.getGenericParameters().size()];
				for(int i = 0; i < types.length; i++){
					types[i] = type.getGenericParameters().get(i).getUpperbound();
				}
				return isAssignableTo(specify(type.getSuperDefinition(), types), target);
			} else {
				return isAssignableTo(type.getSuperDefinition(), target);
			}
		}

		return false;

		//return type.equals(Nothing()) || ( type.getName() == target.getName() && type.getGenericParameters().size() == target.getGenericParameters().size());
	}

	/**
	 * @param progression
	 * @param finalType
	 * @return
	 */
	public SenseTypeDefinition specify(TypeDefinition definition, TypeDefinition ... genericParametersCapture) {

		if (definition.getGenericParameters().size() != genericParametersCapture.length){
			throw new CompilationError("Wrong number of generic arguments for type " + definition + ". Expected " + definition.getGenericParameters().size() + " found " + genericParametersCapture.length);
		}
		GenericTypeParameter[] genericParameters = new GenericTypeParameter[definition.getGenericParameters().size()];

		for (int i =0; i < definition.getGenericParameters().size(); i++){
			GenericTypeParameter gen = definition.getGenericParameters().get(i);
			genericParameters[i] = new FixedGenericTypeParameter(gen.getName(), genericParametersCapture[i], gen.getVariance() ); 
		}

		SenseTypeDefinition concrete = new SenseTypeDefinition(definition.getName(), definition.getKind(), (SenseTypeDefinition)definition.getSuperDefinition(), genericParameters);

		concrete.addMembers(definition.getMembers().stream().map(m -> m.changeDeclaringType(concrete))); 

		return concrete;
	}

	/**
	 * @param left
	 * @param right
	 * @return
	 */
	public boolean isPromotableTo(TypeDefinition a, TypeDefinition b) {
		if (a == b || a.equals(b)){
			return true;
		} else if (isAssignableTo(a, b)){
			return true;
		}
		return !a.getMethodsByName("to" + b.getSimpleName()).isEmpty();
	}

	/**
	 * @param typeDefinition
	 * @param typeDefinition2
	 * @return
	 */
	public TypeDefinition unionOf(TypeDefinition typeDefinition, TypeDefinition typeDefinition2) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

















}
