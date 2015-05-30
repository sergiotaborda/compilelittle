/**
 * 
 */
package compiler.parser;

/**
 * 
 */
public class SLRAutomatonFactory implements ItemStateAutomatonFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStateAutomaton create() {
		return new SLRAutomaton();
	}

}
