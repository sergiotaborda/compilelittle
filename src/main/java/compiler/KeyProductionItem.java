/**
 * 
 */
package compiler;

import compiler.parser.ProductionItem;

/**
 * 
 */
public class KeyProductionItem {

	private ProductionItem item;

	public KeyProductionItem (ProductionItem item){
		this.item = item;
	}
	
	public boolean equals(Object other){
		return other instanceof KeyProductionItem 
				&& ((KeyProductionItem)other).item.equalsIgnoreLookAhead(this.item);
	}
	
	public int hashCode(){
		return item.ignoreLooakAheadHashCode();
	}
}
