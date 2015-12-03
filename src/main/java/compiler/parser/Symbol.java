package compiler.parser;

import java.util.Optional;

import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;

public interface Symbol {

	public Optional<Object> getSemanticAttribute(String name);
	
	public void setSemanticAttribute(String name, Object value);
	
	public <A extends AstNode> Optional<A> getAstNode(Class<A> type);
	public Optional<AstNode> getAstNode();

	public void setAstNode(AstNode node);
	
	public ParserTreeNode getParserTreeNode();

	/**
	 * @return
	 */
	public String getLexicalValue();
}
