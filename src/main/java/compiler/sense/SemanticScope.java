/**
 * 
 */
package compiler.sense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compiler.sense.typesystem.SenseType;
import compiler.typesystem.Type;
import compiler.typesystem.VariableInfo;

/**
 * 
 */
public class SemanticScope {

	private SemanticScope parent;
	private List<SemanticScope> scopes = new ArrayList<SemanticScope>();
	private Map<String, VariableInfo > variables= new HashMap<String, VariableInfo>();
	private String name;

	
	/**
	 * Constructor.
	 * @param scope
	 */
	public SemanticScope(SemanticScope scope) {
		this.parent = scope;
		scope.addChild(this);
	}
	
	/**
	 * Constructor.
	 * @param scope 
	 * @param name 
	 */
	public SemanticScope(String name, SemanticScope scope) {
		this.name = name;
		this.parent = scope;
		scope.addChild(this);
	}

	/**
	 * Constructor.
	 * @param name2
	 */
	public SemanticScope(String name) {
		this.name = name;
	}

	public String toString(){
		return name;
	}
	/**
	 * @param semanticScope
	 */
	private void addChild(SemanticScope semanticScope) {
		scopes.add(semanticScope);
	}
	



	/**
	 * @param id
	 * @param type
	 */
	public VariableInfo defineVariable(String name, Type type) {
		
		if (variables.containsKey(name)){
			throw new SyntaxError("Varible " + name + " is already defined in this scope.");
		}
	   final VariableInfo variableInfo = new VariableInfo(name, type, false);
	   variables.put(name, variableInfo);
	   return variableInfo;
	}

	/**
	 * @param string
	 * @return
	 */
	public VariableInfo searchVariable(String name) {
		VariableInfo info = variables.get(name);
		
		if (info == null && parent != null){
			return this.parent.searchVariable(name);
		}
		
		return info;
	}

	/**
	 * @param name
	 * @param any
	 */
	public VariableInfo defineTypeVariable(String name, SenseType type) {
		if (variables.containsKey(name)){
			throw new SyntaxError("Type varible " + name + " is already defined in this scope.");
		}
	   final VariableInfo variableInfo = new VariableInfo(name, type, true);
	   variables.put(name, variableInfo);
	   
	   return variableInfo;
	}

	/**
	 * @return
	 */
	public SemanticScope getParent() {
		return parent;
	}

}
