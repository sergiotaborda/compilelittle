/**
 * 
 */
package compiler.sense;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import compiler.sense.typesystem.SenseTypeDefinition;
import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.TypeResolver;
import compiler.typesystem.TypeSearchParameters;

/**
 * 
 */
public class SenseTypeResolver implements TypeResolver {

	
	private static SenseTypeResolver me = new SenseTypeResolver();
	private Map<TypeSearchParameters, SenseTypeDefinition> types = new HashMap<>();
	
	/**
	 * @return
	 */
	public static SenseTypeResolver getInstance() {
		return me;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SenseTypeDefinition resolveTypeByName(TypeSearchParameters filter) {
		String name = filter.getName();
		if (!name.contains(".")){
			name = "sense.lang." + name;
		}
		
		SenseTypeDefinition def = types.get(filter);
		if (def == null){
			Optional<SenseTypeDefinition> sdef = SenseTypeSystem.getInstance().getForName(name, filter.getGenericParametersCount());
			if (sdef.isPresent()){
				def=  sdef.get();
				types.put(filter, def);
				
			} else {
				return null;
			}
		}
		
		return def;
		
		
	}
	
//	public void registerType(String name , SenseTypeDefinition type){
//		types.put(name, type);
//	}


}
