/**
 * 
 */
package compiler.trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 
 */
public abstract class AbstractNode<N extends AbstractNode<N>> implements Node<N> {

	private List<N> children = new ArrayList<>();
	private N parent;
	private Map<String, Object> properties = new HashMap<>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public N getParent() {
		return parent;
	}

	protected void setParent(N parent) {
		this.parent = parent;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<N> getChildren() {
		return children;
	}
	
	/**
	 * @param astNode
	 */
	public void add(N node) {
		node.setParent((N)this);
		children.add(node);
	}
	/**
	 * @param astNode
	 */
	public void addFirst(N astNode) {
		List<N> newList = new ArrayList<>(children.size() + 1);
		newList.add(astNode);
		newList.addAll(children);
		this.children = newList;
	}
	
	public void addLast(N astNode) {
		this.add(astNode);
	}
	
	/**
	 * @param astNode
	 */
	public void remove(N node) {
		if (children.remove(node)){
			node.setParent(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <P> Optional<P> getProperty(String name, Class<P> type) {
		if (properties.containsKey(name)){
			return Optional.of(type.cast(properties.get(name)));
		} else {
			return Optional.empty();
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <P> void setProperty(String name, P value) {
		properties.put(name, value);
	}

	public void copyAttributes(AbstractNode<?> other) {
		 for(Map.Entry<String, Object> entry : properties.entrySet()){
			 other.setProperty(entry.getKey(), entry.getValue());
		 }
	}
	
	public void copyAttributes(Consumer<Entry<String, Object>> consumer) {
		 for(Map.Entry<String, Object> entry : properties.entrySet()){
			 consumer.accept(entry);
		 }
	}
	
}
