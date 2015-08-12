/**
 * 
 */
package compiler.sense;

import java.util.ArrayList;
import java.util.List;

import compiler.sense.typesystem.MethodParameter;
import compiler.sense.typesystem.Method;
import compiler.sense.typesystem.ConcreteMethodParameter;
import compiler.sense.typesystem.SenseType;
import compiler.sense.typesystem.TypeMatch;
import compiler.sense.typesystem.UnionType;
import compiler.typesystem.Field;
import compiler.typesystem.Type;
import compiler.typesystem.TypeParameter;

/**
 * 
 */
public class TypeConverter  implements compiler.typesystem.Type {

	private JavaToSenseTypeSystemAdapter resolver;
	private Type javaType;

	public TypeConverter (Type javaType, JavaToSenseTypeSystemAdapter resolver){
		this.resolver = resolver;
		this.javaType = javaType;
	}
	
	
	public String toString(){
		return "¬[" + javaType.getName() + "]";
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return javaType.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeMatch matchAssignableTo(Type other) {
		throw new UnsupportedOperationException("Not implememented yet"); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type getSuperType() {
		 return resolver.resolveType(javaType.getSuperType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type of(Type ... parameterType) {
		return  resolver.resolveType(javaType.of(parameterType));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type or(Type other) {
		return  new UnionType(this, other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAssignableTo(Type other) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Method> getDeclaredMethods(String name) {
		
		List<Method> originals = javaType.getDeclaredMethods(name);
		List<Method> res = new ArrayList<Method>(originals.size());
		
		for ( Method m : originals){

			if (m.getName().equals(name)){
				ConcreteMethodParameter[] all = new ConcreteMethodParameter[ m.getParameters().size()];
				for (int p =0; p < m.getParameters().size(); p++){
					MethodParameter mp = m.getParameters().get(p);
					
					all[p] = new ConcreteMethodParameter( resolver.resolveType(mp.getType()), mp.getName());
				}
				Type type = resolver.resolveType(m.getReturningType());
				
				if (canBeNull(m.getReturningType())){
					res.add(new Method(this,m.getName(), SenseType.Maybe.of(type),  all));
				} else {
					res.add(new Method(this,m.getName(), type,  all));
				}
				
				
			}
			
		
		}
		
		return res;
	}

	/**
	 * @param returningType
	 * @return
	 */
	private boolean canBeNull(Type returningType) {
		 return !returningType.isPrimitive();
	}
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Method> getAvailableMethods(String name) {
		List<Method> list = getDeclaredMethods(name);
		if (list.isEmpty() && javaType.getSuperType() != null){
			return this.getSuperType().getAvailableMethods(name);
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Field> getAvailableFields(String name) {
		List<Field> list = getDeclaredFields(name);
		if (list.isEmpty() && javaType.getSuperType() != null){
			return this.getSuperType().getAvailableFields(name);
		}
		return list;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Field> getDeclaredFields(String name) {
		List<Field> originals = javaType.getDeclaredFields(name);
		List<Field> res = new ArrayList<Field>(originals.size());
		
		for ( Field m : originals){

			if (m.getName().equals(name)){
				
				res.add(new Field(this,m.getName(), resolver.resolveType(m.getType())));
			}
			
		
		}
		
		return res;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPromotableTo(Type type) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSimpleName() {
		return javaType.getSimpleName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TypeParameter> getParameters() {
		throw new UnsupportedOperationException("Not implememented yet");
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
		return javaType.getKind();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGeneric() {
		return javaType.isGeneric();
	}






}
