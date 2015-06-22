/**
 * 
 */
package compiler.parser;

import java.util.Collection;


/**
 * 
 */
public interface LookupTable extends Iterable<LookupTableRow> {

	public LookupTableAction getAction(StackItem item, StackItem peek);

	/**
	 * @param stateId
	 * @return
	 */
	public LookupTableRow getRow(int stateId);

	
	public int rowCount();
	
	public Collection<Production> columns ();

	/**
	 * @param targetId
	 * @return
	 */
	public ProductionItem getFinalProductionItem(int targetId);
}