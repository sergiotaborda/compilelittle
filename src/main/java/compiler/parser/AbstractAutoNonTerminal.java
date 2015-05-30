/**
 * 
 */
package compiler.parser;

/**
 * 
 */
public abstract class AbstractAutoNonTerminal extends NonTerminal implements AutoNonTerminal {

	/**
	 * Constructor.
	 * @param name
	 */
	public AbstractAutoNonTerminal(String name) {
		super(name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAutoNonTerminal() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNonTerminal() {
		return false;
	}
	
	public Production getRule (){
		throw new RuntimeException("Cannot call getRule on a AuntoNonTerminal");
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isText() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIdentifier() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNumeric() {
		return false;
	}

}
