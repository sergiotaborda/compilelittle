/**
 * 
 */
package compiler.parser;

/**
 * 
 */
class ItemStateProductionPair {

	/**
	 * Constructor.
	 * @param current
	 * @param p
	 */
	public ItemStateProductionPair(ItemState current, Production p) {
		item = current;
		production = p;
	}
	public Production production;
	public ItemState item;

}
