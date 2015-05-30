/**
 * 
 */
package compiler.parser;

import compiler.Grammar;

/**
 * 
 */
public interface ItemStateAutomaton {

	
	LookupTable produceLookupTable(Grammar grammar);
}
