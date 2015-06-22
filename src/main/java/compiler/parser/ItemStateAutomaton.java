/**
 * 
 */
package compiler.parser;

import compiler.Grammar;

/**
 * 
 */
public interface ItemStateAutomaton {

	
	ItemStatesLookupTable produceLookupTable(Grammar grammar);
}
