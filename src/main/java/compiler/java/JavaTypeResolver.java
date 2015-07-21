/**
 * 
 */
package compiler.java;

import java.util.HashMap;
import java.util.Map;

import compiler.typesystem.Type;
import compiler.typesystem.TypeResolver;

/**
 * 
 */
public class JavaTypeResolver implements TypeResolver{

	
	static JavaTypeResolver me = new JavaTypeResolver();
	
	private Map<String, Type> types = new HashMap<>();
	/**
	 * @return
	 */
	public static TypeResolver getInstance() {
		return me;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type resolveTypeByName(String name) {
		
		if (!name.startsWith("java")){
			return null;
		}
		
		try {
			return fromClass(Class.forName(name, false, this.getClass().getClassLoader()));
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	

	
	/**
	 * @param forName
	 * @return
	 */
	protected Type fromClass(Class<?> type) {
	
		 if (!types.containsKey(type.getName())){
			 Type t = new JavaType(type, this);
			 types.put(type.getName(), t);
			 return t;
		 }  else {
			 return types.get(type.getName());
		 }
	}



	
}
