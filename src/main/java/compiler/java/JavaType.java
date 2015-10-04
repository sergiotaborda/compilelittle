/**
 * 
 */
package compiler.java;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import compiler.sense.typesystem.GenericTypeParameter;
import compiler.sense.typesystem.Kind;
import compiler.typesystem.Field;
import compiler.typesystem.Method;
import compiler.typesystem.MethodSignature;
import compiler.typesystem.Property;
import compiler.typesystem.TypeDefinition;
import compiler.typesystem.TypeMember;

/**
 * 
 */
public class JavaType implements TypeDefinition {

	public static final Object Object = null;

	/**
	 * Constructor.
	 * @param type
	 * @param javaTypeResolver
	 */
	public JavaType(Class<?> type, JavaTypeResolver javaTypeResolver) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSimpleName() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Kind getKind() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TypeMember> getMembers() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getSuperDefinition() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GenericTypeParameter> getGenericParameters() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Field> getFieldByName(String name) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Property> getPropertyByName(String fieldName) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Method> getMethodsByName(String string) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Method> getMethodBySignature(MethodSignature signature) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Method> getMethodByPromotableSignature(
			MethodSignature signature) {
		throw new UnsupportedOperationException("Not implememented yet");
	}


}
