/**
 * 
 */
package compiler.typesystem;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class CompositeTypeResolver implements TypeResolver {

	
	private List<TypeResolver> resolvers = new LinkedList<>();

	public CompositeTypeResolver (){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition resolveTypeByName(TypeSearchParameters filter) {
		for(TypeResolver resolver : resolvers){
			TypeDefinition type = resolver.resolveTypeByName(filter);
			if (type != null){
				return type;
			}
		}
		return null;
	}

	/**
	 * @param resolver
	 */
	public void addTypeResolver(TypeResolver resolver) {
		 resolvers.add(resolver);
	}

}
