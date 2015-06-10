/**
 * 
 */
package compiler.sense;

import compiler.sense.typesystem.Type;

/**
 * 
 */
public class TypeNode extends SenseAstNode implements TypedNode{

	private boolean isVoid;
	private QualifiedNameNode name;
	private Type type;

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
	public TypeNode(QualifiedNameNode name) {
		this.name = name;
	}

	/**
	 * @param generic
	 */
	public void setParametricTypes(ParametricTypesNode generic) {
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
	public Type getType() {
		return this.type;
	}
	
	public void setType(Type type){
		this.type = type;
	}

}
