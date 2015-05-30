/**
 * 
 */
package compiler.parser;

import java.util.List;



/**
 * 
 */
public interface Production {

	String getLabel();
	
	ProductionSequence add(Production other);
	ProductionAlternative or(Production other);
    Production optional();
    Production oneOrMore();
    Production noneOrMore();
    
    NonTerminal toNonTerminal();
    Terminal toTerminal();
	ProductionAlternative toAlternative();
	ProductionSequence toSequence();
	AutoNonTerminal toAutoNonTerminal();

	
	public Production addSemanticAction(SemanticAction action);
	/**
	 * @return
	 */
	boolean isTerminal();
	/**
	 * @return
	 */
	boolean isSequence();
	/**
	 * @return
	 */
	boolean isAlternative();
	/**
	 * @return
	 */
	boolean isNonTerminal();
	
	boolean isAutoNonTerminal();
	
	boolean isEmpty();
	
	/**
	 * @param ctx
	 * @param object
	 */
	//void execute(ParserContext ctx, Consumer<ParserContext> tail);
	
	/**
	 * @return
	 */
	boolean isMultiple();

	List<SemanticAction> getSemanticActions();


	
    
}
