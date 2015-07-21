/**
 * 
 */
package compiler.typesystem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import compiler.sense.Kind;
import compiler.sense.typesystem.Method;
import compiler.sense.typesystem.MethodSignature;
import compiler.sense.typesystem.TypeMatch;
import compiler.sense.typesystem.TypeParameter;

/**
 * 
 */
public interface Type {

	public String getName();

	public TypeMatch matchAssignableTo(Type other);

	/**
	 * @return
	 */
	public Type getSuperType();

	/**
	 * Sets the type parameters of a generic type
	 * @param parameterType
	 * @return
	 */
	public Type of(Type ... parameterType);

	/**
	 * @param type
	 * @return
	 */
	public Type or(Type other);

	/**
	 * @param type
	 * @return
	 */
	public boolean isAssignableTo(Type other);

	public List<Method> getDeclaredMethods(String name);

	/**
	 * @param name2
	 */
	public List<Method> getAvailableMethods(String name);

	public default Optional<Method> getAppropriateMethod(MethodSignature aparentSignature) {
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
			return Optional.ofNullable(topCandidate);
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
	public boolean isPromotableTo(Type type);

	/**
	 * @return
	 */
	public java.lang.String getSimpleName();

	public List<TypeParameter> getParameters();

	/**
	 * @return
	 */
	public boolean isPrimitive();

	public Kind getKind();

	/**
	 * @return
	 */
	public boolean isGeneric();

	/**
	 * @param fieldName
	 * @return
	 */
	public List<Field> getAvailableFields(String fieldName);

	public List<Field> getDeclaredFields(String name);
}