/**
 * 
 */
package compiler.parser;

import java.util.LinkedList;
import java.util.List;

import compiler.parser.nodes.ParserTreeNode;

/**
 * 
 */
public interface ParsingContext {

	ParserStack stack();
	StackInputStream inputStream();
	/**
	 * @param size
	 * @return
	 */
	List<ParsingContext> split(int size);
	/**
	 * 
	 */
	void invalidate();
	
	boolean isValid();
	/**
	 * @return
	 */
	ParserTreeNode accept();
	/**
	 * @param target
	 * @param left
	 * @param read
	 */
	void addDeferedSemanticAction(ProductionItem target, ProductionStackItem left, LinkedList<Symbol> read);
}
