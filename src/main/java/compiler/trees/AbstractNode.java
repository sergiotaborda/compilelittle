/**
 * 
 */
package compiler.trees;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import compiler.lexer.ScanPosition;
import compiler.lexer.ScanPositionHolder;

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

	public ListIterator<N> listIterator() {
		return new NodeListIterator(this.children.listIterator());
	}

	
	private class NodeListIterator implements ListIterator<N> {

		private ListIterator<N> original;

		public NodeListIterator(ListIterator<N> original) {
			this.original = original;
		}

		@Override
		public void add(N e) {
			original.add(prepareAttach(e));
		}
		

		@Override
		public void set(N e) {
			original.set(prepareAttach(e));
		}

		@Override
		public boolean hasNext() {
			return original.hasNext();
		}

		@Override
		public boolean hasPrevious() {
			return original.hasPrevious();
		}

		@Override
		public N next() {
			return original.next();
		}

		@Override
		public int nextIndex() {
			return original.nextIndex();
		}

		@Override
		public N previous() {
			return original.previous();
		}

		@Override
		public int previousIndex() {
			return original.previousIndex();
		}

		@Override
		public void remove() {
			original.remove();
		}


		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<N> getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	public N getFirstChild() {
		return children.get(0);
	}

	/**
	 * @param astNode
	 */
	public final void add(N node) {
		if (node != null){
			children.add(prepareAttach(node));
		}
	}

	
	protected N prepareAttach(N node){
		if (children.isEmpty()){
			this.setScanPosition(node.getScanPosition());
		}

		node.setParent((N)this);
		return node;
	}
	/**
	 * @param astNode
	 */
	public void addFirst(N node) {
		if (node != null){
			node.setParent((N)this);

			List<N> newList = new ArrayList<>(children.size() + 1);
			newList.add(node);
			newList.addAll(children);
			this.children = newList;
		}
	}

	public void addBefore(N reference, N toinclude) {
		if (toinclude != null){
			toinclude.setParent((N)this);

			int pos = children.indexOf(reference);
			if (pos <= 0){
				this.addFirst(toinclude);
			} else {
				List<N> newList = new LinkedList<>(children);
				newList.add(pos, toinclude);
				this.children = newList;
			}
		}
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
				newnode.setParent((N)this);
				it.remove();
				it.add(newnode);
			}

		}
		
		Stream.of(this.getClass().getDeclaredFields()).filter(f -> Modifier.isPrivate( f.getModifiers())).forEach(f -> {
			
			Object value;
			try {
				f.setAccessible(true);
				value = f.get(this);
				if (value.equals(node)){
					newnode.setParent((N)this);
					f.set(this, newnode); 
				}
			} catch (Exception e) {
				//no-op
			}
			
		});
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
