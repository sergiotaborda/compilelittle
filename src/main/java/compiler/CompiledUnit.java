/**
 * 
 */
package compiler;

import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;

/**
 * 
 */
public final class CompiledUnit {

	private CompilationUnit unit;
	private ParserTreeNode parserTreeRoot;
	private AstNode astRootNode;
	
    CompiledUnit(CompilationUnit unit, ParserTreeNode parserTreeRoot,AstNode astRootNode) {
		this.unit = unit;
		this.parserTreeRoot = parserTreeRoot;
		this.astRootNode = astRootNode;
	}
	
	public CompilationUnit getUnit() {
		return unit;
	}

	public ParserTreeNode getParserTreeRoot() {
		return parserTreeRoot;
	}

	public AstNode getAstRootNode() {
		return astRootNode;
	}
	
//	public CompiledUnit transformAST(Function <AstNode , AstNode> transform){
//		return new CompiledUnit(unit, parserTreeRoot, transform.apply(this.astRootNode));
//	}
	
	public String toString() {
		return unit.getName();
	}
}
