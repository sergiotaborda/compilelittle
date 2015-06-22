/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.MethodParameter;
import compiler.sense.typesystem.Type;

/**
 * 
 */
public class JavaTypeResolver implements TypeResolver{

	
	static JavaTypeResolver me = new JavaTypeResolver();
	

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
		
		if (name.equals("java.util.Math")){
			Type math = new JavaType(name);
			
			math.addMethod("sin",JavaType.PrimitiveDouble, new MethodParameter(JavaType.PrimitiveDouble ));
			return math;
		}
		
		return null;
	}
	

	
	static class JavaType extends Type {

		static JavaType PrimitiveDouble = new JavaType("double", true);
		
		
		private boolean primitive; 
		/**
		 * Constructor.
		 * @param name
		 */
		public JavaType(java.lang.String name) {
			super(name);
		}
		
		public JavaType(java.lang.String name, boolean primitive) {
			super(name);
			this.primitive = primitive;
		}
		public boolean isPrimitive() {
			return primitive;
		}
		public void setPrimitive(boolean primitive) {
			this.primitive = primitive;
		}
		
	}
}
