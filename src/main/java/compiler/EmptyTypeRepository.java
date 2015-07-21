/**
 * 
 */
package compiler;

import compiler.sense.typesystem.SenseType;
import compiler.typesystem.TypesRepository;

/**
 * 
 */
public class EmptyTypeRepository implements TypesRepository {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SenseType resolveTypeByName(String name) {
		return null;
	}

}
