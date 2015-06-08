/**
 * 
 */
package compiler.sense;

import java.util.HashMap;
import java.util.Map;

import compiler.sense.typesystem.Type;

/**
 * 
 */
public class SenseTypeResolver implements TypeResolver {

	
	private static SenseTypeResolver me = new SenseTypeResolver();
	private Map<String, Type> types = new HashMap<>();
	
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
	public Type resolveTypeByName(String name) {
		return types.get(name);
	}
	
	public void registerType(String name , Type type){
		types.put(name, type);
	}


}
