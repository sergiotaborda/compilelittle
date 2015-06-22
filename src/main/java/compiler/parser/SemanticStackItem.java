package compiler.parser;

import java.util.Optional;

import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;

public abstract class SemanticStackItem extends ParserTreeNode implements StackItem , Symbol {

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParserTreeNode getParserTreeNode() {
		return this;
	}
	
	public Optional<Object> getSemanticAttribute(String name)
	{
		return this.getProperty(name, Object.class);
		
	}
	
	public void setSemanticAttribute(String name, Object value)
	{
		this.setProperty(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <A extends AstNode> Optional<A> getAstNode(Class<A> type) {
		return this.getProperty("node", type);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<AstNode> getAstNode() {
		return this.getProperty("node", AstNode.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAstNode(AstNode node) {
		this.setProperty("node", node);
	}
	
	public String getLexicalValue(){
		return this.getProperty("lexicalValue", String.class).orElse(null);
	}


}
