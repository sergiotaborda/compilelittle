/**
 * 
 */
package compiler;

import java.util.ArrayList;
import java.util.List;

import compiler.typesystem.Type;
import compiler.typesystem.TypeResolver;
import compiler.typesystem.TypesRepository;

/**
 * 
 */
public class CompositeTypesRepository implements TypesRepository {

	private List<TypeResolver> typeResolvers = new ArrayList<>();
	
	public CompositeTypesRepository (){}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type resolveTypeByName(String name) {
		Type type = null;
		for (TypeResolver typeResolver : typeResolvers){
			type = typeResolver.resolveTypeByName(name);
			if (type != null){
				
				return type;
			}
		}
		return type;
	}


	public CompositeTypesRepository addTypeResolver(TypeResolver resolver){
		this.typeResolvers.add(resolver);
		return this;
	}
}
