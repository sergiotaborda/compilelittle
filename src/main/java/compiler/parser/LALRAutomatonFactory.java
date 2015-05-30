/**
 * 
 */
package compiler.parser;

/**
 * 
 */
public final class LALRAutomatonFactory implements ItemStateAutomatonFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStateAutomaton create() {
		return new LALRAutomaton();
	}

}
