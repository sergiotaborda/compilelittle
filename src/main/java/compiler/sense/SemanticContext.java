/**
 * 
 */
package compiler.sense;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import compiler.sense.typesystem.Type;

/**
 * 
 */
public class SemanticContext {

	Deque<SemanticScope> scopes = new LinkedList<SemanticScope>();
	private List<String> imports = new ArrayList<String>();
	private List<TypeResolver> typeResolvers = new ArrayList<>();
	private Map<String, Type> types = new HashMap<>();

	public SemanticContext(){
		typeResolvers.add(SenseTypeResolver.getInstance());
		typeResolvers.add(JavaTypeResolver.getInstance());
		imports.add("sense");
	}

	public SemanticContext addTypeResolver(TypeResolver resolver){
		this.typeResolvers.add(resolver);
		return this;
	}


	/**
	 * 
	 */
	public void beginScope(String name) {
		SemanticScope scope = scopes.peek();
		if (scope != null){
			scopes.addFirst(new SemanticScope(name, scope));
		} else {
			scopes.addFirst(new SemanticScope(name));
		}
	}

	public void endScope(){
		scopes.removeFirst();
	}

	public SemanticScope currentScope(){
		return scopes.getFirst();
	}

	/**
	 * @param name
	 */
	public void addImportPackage(String importName) {
		imports.add(importName);
	}

	/**
	 * @param name
	 * @return
	 */
	public Type typeForName(String name) {

		if(name.contains(".")){
			// is qualified
			Type type = typeForQualifiedName(name);
			if (type!= null){
				return type;
			} else {
				throw new SyntaxError(name + " is not a recognized type");
			}
		} else {

			// try type variable
			VariableInfo variableInfo = currentScope().searchVariable(name);

			if (variableInfo != null && variableInfo.isTypeVariable()){
				return new GenericType(name, variableInfo.getType());
			}


			// not type variable, attach imports and look again

			for (String importPackage : imports){
				Type type = typeForQualifiedName(importPackage + "." + name);
				if (type != null){
					return type;
				}
			}
			throw new SyntaxError(name + " is not a recognized type");
		}

	}

	/**
	 * @param name
	 * @return
	 */
	private Type typeForQualifiedName(String name) {

		Type type = types.get(name);


		if (type == null){
			for (TypeResolver typeResolver : typeResolvers){
				type = typeResolver.resolveTypeByName(name);
				if (type != null){
					types.put(name, type);
					return type;
				}
			}
		}
		return type;
	}

}
