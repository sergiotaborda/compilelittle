/**
 * 
 */
package compiler.sense.typesystem;

import java.util.Collections;
import java.util.List;

import compiler.sense.Kind;
import compiler.typesystem.Field;
import compiler.typesystem.Type;
import compiler.typesystem.TypeParameter;

/**
 * 
 */
public class NothingType extends SenseType {

	/**
	 * Constructor.
	 * @param name
	 */
	protected NothingType() {
		super("sense.Nothing");
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
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type of(Type ... parameterType) {
		throw new UnsupportedOperationException("Nothing is not generic");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type or(Type other) {
		throw new UnsupportedOperationException("Nothing is not generic");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAssignableTo(Type other) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Method> getDeclaredMethods(String name) {
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Method> getAvailableMethods(String name) {
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPromotableTo(Type type) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSimpleName() {
		return "Nothing";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TypeParameter> getParameters() {
		return Collections.emptyList();
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
		return Kind.Class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGeneric() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Field> getAvailableFields(String fieldName) {
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Field> getDeclaredFields(String name) {
		return Collections.emptyList();
	}

}
