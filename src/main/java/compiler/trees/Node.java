/**
 * 
 */
package compiler.trees;

import java.util.List;
import java.util.Optional;


/**
 * 
 */
public interface Node<N extends Node<N>> {

	public N getParent();
	
	public List<N> getChildren();
	
	public <P> Optional<P> getProperty(String name, Class<P> type);
	
	public <P> void setProperty(String name, P value);
	
	public void copyAttributesTo(AbstractNode<?> other);

	public void add(N node);

	/**
	 * @param parent
	 */
	public void remove(N parent);
}
