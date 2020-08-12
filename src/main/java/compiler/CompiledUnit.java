/**
 * 
 */
package compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;

/**
 * 
 */
public final class CompiledUnit {

	private CompilationUnit unit;
	private ParserTreeNode parserTreeRoot;
	private AstNode astRootNode;
	
	private Map<String,Object> properties = new HashMap<>(); 
	
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
	
    public <P> Optional<P> getProperty(String name, Class<P> type) {
        if (properties.containsKey(name)){
            try {
                P p = type.cast(properties.get(name));
                return Optional.of(p);
            } catch (ClassCastException e){
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }

    }

    public <P> void setProperty(String name, P value) {
        properties.put(name, value);
    }

	public String toString() {
		return unit.getName();
	}
}
