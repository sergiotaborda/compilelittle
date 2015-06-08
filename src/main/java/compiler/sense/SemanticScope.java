/**
 * 
 */
package compiler.sense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compiler.sense.typesystem.Type;

/**
 * 
 */
public class SemanticScope {

	private SemanticScope parent;
	private List<SemanticScope> scopes = new ArrayList<SemanticScope>();
	private Map<String, VariableInfo > variables= new HashMap<String, VariableInfo>();

	/**
	 * Constructor.
	 * @param scope
	 */
	public SemanticScope(SemanticScope scope) {
		this.parent = scope;
		scope.addChild(this);
	}

	/**
	 * @param semanticScope
	 */
	private void addChild(SemanticScope semanticScope) {
		scopes.add(semanticScope);
	}
	

	/**
	 * Constructor.
	 */
	public SemanticScope() {
	
	}

	/**
	 * @param id
	 * @param type
	 */
	public VariableInfo defineVariable(String name, Type type) {
		
		if (variables.containsKey(name)){
			throw new SyntaxError("Varible " + name + " is already defined in this scope.");
		}
	   final VariableInfo variableInfo = new VariableInfo(name, type);
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

}
