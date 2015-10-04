/**
 * 
 */
package compiler.sense.typesystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import compiler.sense.ast.Imutability;
import compiler.typesystem.Field;
import compiler.typesystem.FixedTypeVariable;
import compiler.typesystem.Method;
import compiler.typesystem.MethodParameter;
import compiler.typesystem.MethodReturn;
import compiler.typesystem.MethodSignature;
import compiler.typesystem.Property;
import compiler.typesystem.TypeDefinition;
import compiler.typesystem.TypeMember;

/**
 * 
 */
public class SenseTypeDefinition implements TypeDefinition {

	private String name;
	private Kind kind;
	private List<TypeMember> members= new ArrayList<TypeMember>();
	private List<GenericTypeParameter> genericParameters;
	private SenseTypeDefinition superDefinition;

	public SenseTypeDefinition (String name, Kind kind, SenseTypeDefinition superDefinition){
		this.name = name;
		this.kind = kind;
		this.superDefinition = superDefinition;
		this.genericParameters = superDefinition == null ? Collections.emptyList() : superDefinition.getGenericParameters();
	}

	public SenseTypeDefinition (String name, Kind kind, SenseTypeDefinition superDefinition, GenericTypeParameter ... parameters){
		this.name = name;
		this.kind = kind;
		this.superDefinition = superDefinition;
		this.genericParameters = Arrays.asList(parameters);
	}

	public String toString(){
		
		if (genericParameters.isEmpty()){
			return name;
		}
		
		StringBuilder builder = new StringBuilder(name).append("<");
		for(GenericTypeParameter p : genericParameters){
			builder.append(p.getUpperbound().toString()).append(",");
		}
		builder.delete(builder.length()-1, builder.length());
		
		return builder.append(">").toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSimpleName() {
		int pos = name.lastIndexOf('.');
		if (pos >=0){
			return name.substring(pos + 1);
		} else {
			return name;
		}
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
	public List<TypeMember> getMembers() {
		return members;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public SenseTypeDefinition getSuperDefinition() {
		return superDefinition;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GenericTypeParameter> getGenericParameters() {
		return genericParameters;
	}




	/**
	 * @param name2
	 * @param typeDefinition
	 * @param parameters
	 */
	public void addMethod(String name, TypeDefinition returnType, MethodParameter ... parameters) {
		Method m = new Method(name, new MethodReturn(new FixedTypeVariable(returnType)), parameters);
		m.setDeclaringType(this);
		this.members.add(m);
	}

	/**
	 * @param name2
	 * @param typeDefinition
	 * @param imutabilityValue
	 */
	public void addField(String name, TypeDefinition typeDefinition,Imutability imutabilityValue) {
		final Field field = new Field(name, typeDefinition,imutabilityValue);
		field.setDeclaringType(this);
		this.members.add(field);
	}

	/**
	 * @param superType
	 */
	public void setSuperTypeDefinition(TypeDefinition superType) {
		this.superDefinition = (SenseTypeDefinition) superType;
		if (this.genericParameters.size() == 0){
			this.genericParameters = superType.getGenericParameters();
		}
	}


	/**
	 * @param kind2
	 */
	public void setKind(Kind kind) {
		this.kind = kind;
	}




	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Field> getFieldByName(String name) {
		Optional<Field> member = members.stream().filter(m -> m.isField() && m.getName().equals(name)).map(m -> (Field)m).findAny();

		if (!member.isPresent() && this.superDefinition != null){
			return this.superDefinition.getFieldByName(name);
		}

		return member;
	}




	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Property> getPropertyByName(String fieldName) {
		Optional<Property> member = members.stream().filter(m -> m.isProperty() && m.getName().equals(name)).map(m -> (Property)m).findAny();

		if (!member.isPresent() && this.superDefinition != null){
			return this.superDefinition.getPropertyByName(name);
		}

		return member;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Method> getMethodsByName(String name) {
		Collection<Method>  all = members.stream().filter(m -> m.isMethod() && m.getName().equals(name)).map(m -> (Method)m).collect(Collectors.toList());

		if (all.isEmpty() && this.superDefinition != null){
			return this.superDefinition.getMethodsByName(name);
		}

		return all;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Method> getMethodBySignature(MethodSignature signature) {
		// find exact local
		Optional<Method>  member = members.stream().filter(m -> m.isMethod() && m.getName().equals(signature.getName()) && signature.isImplementedBy((Method)m)).map(m -> (Method)m).findAny();

		// find exact upper class
		if (!member.isPresent() && this.superDefinition != null){
			return this.superDefinition.getMethodBySignature(signature);
		}

		return member;
	}
	@Override
	public Optional<Method> getMethodByPromotableSignature(MethodSignature signature)
	{
		// find promotable

		for(Method mth : this.getMethodsByName(signature.getName())){
			if (mth.getParameters().size() == signature.getParameters().size()){
				for (int p =0; p < signature.getParameters().size(); p++){
					MethodParameter mp = signature.getParameters().get(p);
					if (SenseTypeSystem.getInstance().isPromotableTo(mp.getType().getUpperbound(), mth.getParameters().get(p).getType().getLowerBound())){
						return Optional.of(mth);
					}
				}
			}
		}

		return Optional.empty();
	}
	/**
	 * 
	 */
	void addMembers(Stream<TypeMember> all) {
		all.forEach(a -> members.add(a) );
	}



}
