/**
 * 
 */
package compiler.sense;

import java.util.HashMap;
import java.util.Map;

import compiler.sense.typesystem.SenseType;
import compiler.typesystem.Type;
import compiler.typesystem.TypeNotFoundException;
import compiler.typesystem.TypeResolver;

/**
 * 
 */
public class JavaToSenseTypeSystemAdapter implements TypeResolver {

	private TypeResolver original;
	private Map<String, Type> typesCache = new HashMap<>();
	private TypeResolver senseResolver;
	/**
	 * Constructor.
	 * @param instance
	 */
	public JavaToSenseTypeSystemAdapter(TypeResolver original, TypeResolver senseResolver) {
		this.original = original;
		this.senseResolver = senseResolver;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type resolveTypeByName(String name) {
		
		Type transformedType = typesCache.get(name);
		if (transformedType == null){
			
			transformedType = transform( original.resolveTypeByName(name));
			 typesCache.put(name, transformedType);
		} 
		return transformedType;
	}


	/**
	 * @param resolveTypeByName
	 * @return
	 */
	public Type transform(Type javaType) {
		if (javaType == null){
			return null;
		}
		if (javaType.isPrimitive()){
			
			if (javaType.getName().equals("char")){
				return SenseType.Character;
			} 
		
			
			Type primitive =  senseResolver.resolveTypeByName("sense." + javaType.getName().substring(0, 1).toUpperCase() + javaType.getName().substring(1));
			
			if (primitive == null){
				throw new TypeNotFoundException("Primtive java type " + javaType.getName() + " is not mapped to sense.");
			}
			return primitive;
		} else 	if (javaType.getName().contains("java.lang.String")){
			return SenseType.String;
		} else if (javaType.getName().startsWith("[")){
			if (javaType.getName().substring(1, 2).equals("C")){
				return SenseType.Array.or(SenseType.Character);
			}
			return SenseType.Array;
		} else {
			return new TypeConverter(javaType, this);
		}
	}


	/**
	 * @param superType
	 * @return
	 */
	public Type resolveType(Type type) {
		Type transformedType = typesCache.get(type.getName());
		if (transformedType == null){
			
			transformedType = transform( type);
			 typesCache.put(type.getName(), transformedType);
		} 
		return transformedType;
	}



}
