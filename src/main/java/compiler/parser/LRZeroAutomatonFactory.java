/**
 * 
 */
package compiler.parser;

/**
 * 
 */
public class LRZeroAutomatonFactory implements ItemStateAutomatonFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStateAutomaton create() {
		return new LRZeroAutomaton();
	}

}
