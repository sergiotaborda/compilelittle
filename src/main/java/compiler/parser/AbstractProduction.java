/**
 * 
 */
package compiler.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public abstract class AbstractProduction implements Production{

	protected String label;

	protected List<SemanticAction> actions = new ArrayList<SemanticAction>();
	
	public AbstractProduction (){

	}
	

	@Override
	public List<SemanticAction> getSemanticActions() {
		return actions;
	}

	
	public Production addSemanticAction(SemanticAction action)
	{
		actions.add(action);
		return this;
	}
	
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductionSequence add(Production other) {
		return new ProductionSequence(this).add(other);
	}


	public ProductionSequence add(String terminal ) {
		return add(Terminal.of(terminal));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProductionAlternative or(Production other) {
		return new ProductionAlternative(this).or(other);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Production oneOrMore() {
		return new ProductionMultiple(this, false);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Production noneOrMore() {
		return new ProductionMultiple(this, true);
	}
	
	/**
	 * @param string
	 * @return
	 */
	public Production label(String label) {
		this.label = label;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Production optional() {
		return this.or(EmptyTerminal.instance());
	}
	
	public boolean isEmpty(){
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTerminal() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAutoNonTerminal() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSequence() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMultiple() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAlternative() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNonTerminal() {
		return false;
	}

	public String toString(){
		return label;
	}
	

	@Override
	public NonTerminal toNonTerminal() {
		return (NonTerminal)this;
	}

	@Override
	public Terminal toTerminal() {
		return (Terminal)this;
	}
	
	@Override
	public ProductionAlternative toAlternative(){
		return (ProductionAlternative)this;
	}
	
	@Override
	public ProductionSequence toSequence()
	{
		return (ProductionSequence)this;
	}
	
	@Override
	public AutoNonTerminal toAutoNonTerminal() {
		return (AutoNonTerminal)this;
	}

	
}
