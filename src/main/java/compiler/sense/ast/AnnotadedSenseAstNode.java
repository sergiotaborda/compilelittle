/**
 * 
 */
package compiler.sense.ast;


/**
 * 
 */
public class AnnotadedSenseAstNode extends SenseAstNode  {

	private AnnotationListNode annotations;
	
	/**
	 * {@inheritDoc}
	 */
	public void setAnnotations(AnnotationListNode annotations) {
		this.annotations = annotations;
		this.add(annotations);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AnnotationListNode getAnnotations(){
		return annotations;
	}
}
