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
import java.util.Optional;

import compiler.typesystem.Type;
import compiler.typesystem.TypeNotFoundException;
import compiler.typesystem.TypesRepository;

/**
 * 
 */
public class SemanticContext {

	Deque<SemanticScope> scopes = new LinkedList<SemanticScope>();
	private List<String> imports = new ArrayList<String>();
	
	private Map<String, Type> types = new HashMap<>();
	private TypesRepository repository;
	
	public SemanticContext(TypesRepository repository){
		this.repository = repository;

		imports.add("sense");
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

	public Type typeForName(String name) {
		Optional<Type> type = resolveTypeForName(name);
		
		if (!type.isPresent()){
			throw new TypeNotFoundException(name + " is not a recognized type");
		}
		
		return type.get();
	}
	/**
	 * @param name
	 * @return
	 */
	public Optional<Type> resolveTypeForName(String name) {

		if(name.contains(".")){
			// is qualified
			Type type = typeForQualifiedName(name);
			if (type!= null){
				return Optional.of(type);
			} else {
				return Optional.empty();
			}
		} else {

			// try type variable
			VariableInfo variableInfo = currentScope().searchVariable(name);

			if (variableInfo != null && variableInfo.isTypeVariable()){
				return Optional.of(variableInfo.getType());
			}


			// not type variable, attach imports and look again

			for (String importPackage : imports){
				Type type = typeForQualifiedName(importPackage + "." + name);
				if (type != null){
					return Optional.of(type);
				}
			}
			return Optional.empty();
		}

	}

	/**
	 * @param name
	 * @return
	 */
	private Type typeForQualifiedName(String name) {

		Type type = types.get(name);


		if (type == null){
			
			type = repository.resolveTypeByName(name);

			if (type != null){
				types.put(name, type);
			}
		}
		return type;
	}

}
