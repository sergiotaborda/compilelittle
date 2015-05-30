/**
 * 
 */
package compiler.parser;


/**
 * 
 */
public abstract class ProductionVisitor {

	/**
	 * @param n
	 * @param parent 
	 */
	protected VisitorNext visitBeforeNonTerminal(NonTerminal n, Production parent) {
		//no-op
		return VisitorNext.Children;
	}

	/**
	 * @param s
	 * @param parent 
	 */
	protected VisitorNext visitBeforeAlternative(ProductionAlternative s, Production parent) {
		//no-op
		return VisitorNext.Children;
	}

	/**
	 * @param s
	 * @param parent 
	 */
	protected VisitorNext visitBeforeSequence(ProductionSequence s, Production parent) {
		//no-op
		return VisitorNext.Children;
	}

	/**
	 * @param t
	 * @param parent 
	 */
	protected VisitorNext visitTerminal(Terminal t, Production parent) {
		//no-op
		return VisitorNext.Siblings;
	}

	/**
	 * 
	 */
	protected VisitorNext startVisit() {
		//no-op
		return VisitorNext.Children;
	}

	/**
	 * 
	 */
	protected void endVisit() {
		//no-op
	}

	/**
	 * @param s
	 * @param parent 
	 */
	protected VisitorNext visitAfterSequence(ProductionSequence s, Production parent) {
		//no-op
		return VisitorNext.Siblings;
	}

	/**
	 * @param s
	 * @param parent 
	 */
	protected VisitorNext visitAfterAlternative(ProductionAlternative s, Production parent) {
		//no-op
		return VisitorNext.Siblings;
	}

	/**
	 * @param n
	 * @param parent 
	 */
	protected VisitorNext visitAfterNonTerminal(NonTerminal n, Production parent) {
		//no-op
		return VisitorNext.Siblings;
	}
	
	/**
	 * @param n
	 * @param parent 
	 */
	protected VisitorNext visitBeforeAutoNonTerminal(AutoNonTerminal n, Production parent) {
		//no-op
		return VisitorNext.Siblings;
	}

	/**
	 * @param s
	 * @param parent 
	 */
	protected VisitorNext visitBeforeMultiple(ProductionMultiple s, Production parent) {
		//no-op
		return VisitorNext.Children;
	}

	/**
	 * @param s
	 * @param parent 
	 */
	protected VisitorNext visitAfterMultiple(ProductionMultiple s, Production parent) {
		//no-op
		return VisitorNext.Siblings;
	}

	/**
	 * @param n
	 * @param parent
	 */
	protected VisitorNext visitRepeatNonTerminal(NonTerminal n, Production parent) {
		//no-op
		return VisitorNext.Siblings;
	}


}
