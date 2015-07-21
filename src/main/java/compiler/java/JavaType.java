/**
 * 
 */
package compiler.java;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import compiler.sense.Kind;
import compiler.sense.typesystem.ConcreteMethodParameter;
import compiler.sense.typesystem.Method;
import compiler.sense.typesystem.TypeMatch;
import compiler.sense.typesystem.TypeParameter;
import compiler.sense.typesystem.TypeParameter.Variance;
import compiler.sense.typesystem.UnionType;
import compiler.typesystem.Field;
import compiler.typesystem.Type;

/**
 * 
 */
public class JavaType implements Type {

	private Class<?> type;
	private JavaTypeResolver resolver;

	/**
	 * Constructor.
	 * @param type
	 */
	public JavaType(Class<?> type, JavaTypeResolver resolver) {
		this.type = type;
		this.resolver = resolver;
	}
	
	public String toString(){
		return this.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return type.getName();
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
		return type.getSuperclass() == null ? null : resolver.fromClass(type.getSuperclass());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type of(Type ... parameterType) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type or(Type other) {
		return new UnionType(this, other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAssignableTo(Type other) {
		if (other instanceof JavaType){
			JavaType ad = (JavaType)other;
			return ad.type.isAssignableFrom(this.type);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPromotableTo(Type other) {
		if (other instanceof JavaType){
			JavaType ad = (JavaType)other;
			return ad.type.isAssignableFrom(this.type);
		}
		return false;
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Method> getDeclaredMethods(String name) {
		
		final java.lang.reflect.Method[] declaredMethods = type.getDeclaredMethods();
		List<Method> res = new ArrayList<Method>(declaredMethods.length);
		
		for (int i =0; i< declaredMethods.length;i++){
			java.lang.reflect.Method m = declaredMethods[i];
			
			if (m.getName().equals(name)){
				ConcreteMethodParameter[] all = new ConcreteMethodParameter[ m.getParameters().length];
				for (int p =0; p < m.getParameters().length; p++){
					all[p] = new ConcreteMethodParameter(resolver.fromClass(m.getParameterTypes()[p]),  m.getParameters()[p].getName());
				}
				
				res.add(new Method(this,m.getName(), resolver.fromClass(m.getReturnType()),  all));
			}
			
		
		}
		
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Method> getAvailableMethods(String name) {
		List<Method> list = getDeclaredMethods(name);
		if (list.isEmpty() && this.type.getSuperclass() != null){
			return resolver.fromClass(this.type.getSuperclass()).getAvailableMethods(name);
		}
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSimpleName() {
		return type.getSimpleName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TypeParameter> getParameters() {
		final TypeVariable<?>[] typeParameters = type.getTypeParameters();
		List<TypeParameter> res = new ArrayList<TypeParameter>(typeParameters.length);
		
		for (int i =0; i< typeParameters.length;i++){
			res.add(new TypeParameter(Variance.Invariant, "T" + i, 
					resolver.resolveTypeByName(typeParameters[i].getBounds()[0].toString()), 
					resolver.resolveTypeByName(typeParameters[i].getBounds()[1].toString())));
		}
		
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPrimitive() {
		return type.isPrimitive();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Kind getKind() {
		if (type.isAnnotation()){
			return Kind.Annotation;
		} else if (type.isInterface()){
			return Kind.Interface;
		} else if (type.isEnum()){
			return Kind.Enum;
		} else {
			return Kind.Class;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGeneric() {
		return type.getTypeParameters().length > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Field> getAvailableFields(String name) {
		List<Field> list = getDeclaredFields(name);
		if (list.isEmpty() && this.type.getSuperclass() != null){
			return resolver.fromClass(this.type.getSuperclass()).getAvailableFields(name);
		}
		return list;
	}

	/**
	 * @param name
	 * @return
	 */
	public List<Field> getDeclaredFields(String name) {
		final java.lang.reflect.Field[] declaredFields = type.getDeclaredFields();
		List<Field> res = new ArrayList<Field>(declaredFields.length);
		
		for (int i =0; i< declaredFields.length;i++){
			java.lang.reflect.Field m = declaredFields[i];
			
			if (m.getName().equals(name)){
			
				
				res.add(new Field(this,m.getName(), resolver.fromClass(m.getType())));
			}
			
		
		}
		
		return res;
	}

}
