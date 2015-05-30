package compiler.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import compiler.FirstFollowTable;

public class ItemState implements Iterable<ProductionItem>{


	private static int nextId = 0;
	private int id;
	List<ProductionItem> items = new CopyOnWriteArrayList<>();
	Set<ProductionItem> uniqueItems = new HashSet<>();
	FirstFollowTable table;
	private Map<Production, ItemState> transforms = new HashMap<>();

	public ItemState (FirstFollowTable table){
		id = nextId++;
		this.table = table;
	}

	public int getId(){
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	void addAll(List<ProductionItem> all) {
		for(ProductionItem p : all){
			add(p);
		}
	}

	public boolean add(ProductionItem item) {
		if (uniqueItems.add(item)){
			items.add(item);
			return true;
		}
		return false;
	}

	public boolean hasReduceReduceConflit(){
		
		return items.stream().filter(p -> p.isFinal()).count() > 1;
	}

	public boolean hasShiftReduceConflit(){
		
		if( items.stream().anyMatch(p -> p.isFinal())){
			// has reduce
			if( items.stream().anyMatch(p -> p.isShift())){
				return true;
			}
		}
		return false;
	}

	

	

    void mergeItems() {

		if (items.size() > 1){
			Set<ProductionItem> toRemove = new HashSet<>();

			for (int i =0; i < items.size(); i++){
				if (!toRemove.contains(items.get(i))){
					for (int j =i+1; j < items.size(); j++){
						if (items.get(i).equalsIgnoreLookAhead(items.get(j))){
							items.get(i).merge(items.get(j));
							toRemove.add(items.get(j));
						}
					}	
				}
			}

			items.removeAll(toRemove);
			uniqueItems.clear();
			uniqueItems.addAll(items);
		}

	}

	public int hashCode()
	{
		return this.id;
	}

	public boolean equals(Object other){
		return other instanceof ItemState && equalsOther((ItemState)other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsOther(ItemState other) {
		return this.id == other.id || this.itemsEquals(other);
	}

	public Stream<Production> getNextObservables() {

		return items.stream().filter(p -> !p.isFinal())
				.map(p -> p.getNextReadable())
				.filter(next -> !EmptyTerminal.instance().equals(next)).distinct();
	}



	boolean itemsEquals(ItemState other) {
		if (this.items.size() != other.items.size()){
			return false;
		}


		for (ProductionItem item : this.items){

			if (!other.uniqueItems.contains(item)){
				return false;
			}
		}

		return true;
	} 

	public String toString(){
		StringBuilder builder = new StringBuilder("[").append(id).append("]{\n");

		for(ProductionItem p : items){
			builder.append(p.toString()).append("\n");
		}

		return builder.append("}}").toString();
	}

	public boolean isFinal() {
		return items.size() == 1 && items.get(0).isFinal();
	}

	public boolean containsAugmented() {
		return items.size() == 1 && items.get(0).isAugmented();
	}

	public int size() {
		return items.size();
	}

	public ProductionItem getFirst() {
		return items.get(0);
	}

	@Override
	public Iterator<ProductionItem> iterator() {
		return items.iterator();
	}

	public void addStateTransform(Production p, ItemState newState) {
		transforms.put(p, newState);
	}

	public Set<Map.Entry<Production, ItemState>> getTransformations(){
		return transforms.entrySet();
	}

	public boolean observablesMatchTransistions() {
		//Collection<Production> obs = .iterator();
		Set<Production> trans = this.transforms.keySet();

		Iterator<Production> it = this.getNextObservables().iterator();
		while(it.hasNext()){
			if (!trans.contains(it.next())){
				return false;
			}
		}

		return true;
	}

}
