/**
 * 
 */
package compiler.sense;

import java.util.HashMap;
import java.util.Map;

import compiler.sense.typesystem.SenseType;
import compiler.typesystem.TypeResolver;

/**
 * 
 */
public class SenseTypeResolver implements TypeResolver {

	
	private static SenseTypeResolver me = new SenseTypeResolver();
	private Map<String, SenseType> types = new HashMap<>();
	
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
	public SenseType resolveTypeByName(String name) {
		if (!name.contains(".")){
			name = "sense." + name;
		}
		return types.get(name);
	}
	
	public void registerType(String name , SenseType type){
		types.put(name, type);
	}


}
