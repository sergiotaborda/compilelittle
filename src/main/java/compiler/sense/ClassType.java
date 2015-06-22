/**
 * 
 */
package compiler.sense;


/**
 * 
 */
public class ClassType extends SenseAstNode {

	private String name;
	private ClassBodyNode body;
	private TypeNode superType;
	private AnnotationListNode annotationList;
	private ParametricTypesNode parametricTypesNode;
	
	public String getName() {
		return name;
	}
	

	public void setAnnotationList(AnnotationListNode annotationListNode) {
		this.annotationList = annotationListNode;
		this.add(annotationListNode);
	}
	
	public AnnotationListNode getAnnotationListNode(){
		return annotationList;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ClassBodyNode getBody() {
		return body;
	}

	public void setBody(ClassBodyNode body) {
		this.body = body;
		this.add(body);
	}

	public void setSuperType(TypeNode upperType) {
		this.superType = upperType;
		this.add(upperType);
	}

	/**
	 * Obtains {@link TypeNode}.
	 * @return the superType
	 */
	public TypeNode getSuperType() {
		return superType;
	}


	/**
	 * @param parametricTypesNode
	 */
	public void setGenerics(ParametricTypesNode parametricTypesNode) {
		this.parametricTypesNode = parametricTypesNode;
		this.add(parametricTypesNode);
	}


	public ParametricTypesNode getGenerics() {
		return parametricTypesNode;
	}






}
