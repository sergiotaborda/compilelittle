/**
 * 
 */
package compiler.sense;

/**
 * 
 */
public class AnnotadedSenseAstNode extends SenseAstNode {

	private AnnotationListNode annotations;
	
	/**
	 * @param annotationListNode
	 */
	public void setAnnotations(AnnotationListNode annotations) {
		this.annotations = annotations;
		this.add(annotations);
	}
	
	public AnnotationListNode getAnnotations(){
		return annotations;
	}
}
