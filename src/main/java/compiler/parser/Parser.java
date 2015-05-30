package compiler.parser;

import compiler.lexer.TokenStream;
import compiler.parser.nodes.ParserTreeNode;

public interface Parser {

	public abstract ParserTreeNode parse(TokenStream tokens );

}