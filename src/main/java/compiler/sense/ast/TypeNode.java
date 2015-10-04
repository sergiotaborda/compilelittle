/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.GenericTypeParameter;
import compiler.typesystem.TypeDefinition;

/**
 * 
 */
public class TypeNode extends SenseAstNode implements TypedNode{

	private boolean isVoid;
	private QualifiedNameNode name;
	private TypeDefinition type;

	/**
	 * Constructor.
	 * @param b
	 */
	public TypeNode(boolean isVoid) {
		this.isVoid = isVoid;
	}
	/**
	 * Constructor.
	 * @param object
	 */
	public TypeNode(TypeDefinition type) {
		this.name = new QualifiedNameNode(type.getName());
		this.setTypeDefinition(type);
		
		for(GenericTypeParameter p : type.getGenericParameters()){
			this.add(new ParametricTypesNode(new TypeNode(p.getLowerBound()), p.getVariance()));
		}
	}


	/**
	 * Constructor.
	 * @param object
	 */
	public TypeNode(QualifiedNameNode name) {
		this.name = name;
	}

	/**
	 * @param generic
	 */
	public void addParametricType(ParametricTypesNode generic) {
		this.add(generic);
	}

	/**
	 * @return
	 */
	public String getName() {
		return isVoid ? "sense.Void" : name.toString();
	}

	/**
	 * @return
	 */
	public boolean isVoid() {
		return this.isVoid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeDefinition getTypeDefinition() {
		return this.type;
	}
	
	public void setTypeDefinition(TypeDefinition type){
		this.type = type;
	}
	/**
	 * @return
	 */
	public int getGenericParametersCount() {
		if (this.getChildren() == null || this.getChildren().isEmpty()){
			return 0;
		} else {
			return this.getChildren().get(0).getChildren().size();
		}
	}


}
