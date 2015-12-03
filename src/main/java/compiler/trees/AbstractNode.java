/**
 * 
 */
package compiler.trees;

import compiler.lexer.ScanPosition;
import compiler.lexer.ScanPositionHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 
 */
public abstract class AbstractNode<N extends AbstractNode<N>> implements Node<N> , ScanPositionHolder{

	private List<N> children = new ArrayList<>();
	private N parent;
	private Map<String, Object> properties = new HashMap<>();
	private ScanPosition position;
	
	public AbstractNode(){
	}
	
	public ScanPosition getScanPosition(){
		return position;
	}
	
	public void setScanPosition(ScanPosition position){
		this.position = position;
	}
	
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
	public final void add(N node) {
		if (node != null){
			
			if (children.isEmpty()){
				this.setScanPosition(node.getScanPosition());
			}
			
			node.setParent((N)this);
			children.add(node);
		}
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

	public  void replace(N node, N newnode){
		
		for (ListIterator<N> it = children.listIterator(); it.hasNext();) {
			N n = it.next();
			
			if (n.equals(node)){
//				if (n.getParent() == this){
//					n.setParent(null);
//				}
				newnode.setParent((N)this);
				it.remove();
				it.add(newnode);
			}
					
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <P> Optional<P> getProperty(String name, Class<P> type) {
		if (properties.containsKey(name)){
			try {
				P p = type.cast(properties.get(name));
				return Optional.of(p);
			} catch (ClassCastException e){
				return Optional.empty();
			}
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
