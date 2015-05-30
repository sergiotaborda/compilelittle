package compiler.parser.nodes;

import compiler.trees.AbstractNode;

public abstract class ParserTreeNode extends AbstractNode<ParserTreeNode> {

	public abstract String getLabel();

}
