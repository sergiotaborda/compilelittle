/**
 * 
 */
package compiler;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import compiler.lexer.Token;
import compiler.lexer.TokenStream;
import compiler.parser.Parser;
import compiler.parser.nodes.ParserTreeNode;

/**
 * 
 */
public class Compiler {
	
	private Language language;
	private List<CompilerBackEnd> ends = new ArrayList<>();

	public Compiler(Language language){
		this.language = language;
	}
	
	public void addBackEnd(CompilerBackEnd end){
		this.ends.add(end);
	}
	
	public void compile(CompilationUnitSet unitSet) throws IOException{
		
		if (ends.isEmpty()){
			throw new RuntimeException("No backend is attached");
		}
		Grammar grammar = language.getGrammar();
		
		Parser p = grammar.parser();
		
		for(CompilationUnit unit : unitSet){
			try ( Reader reader = unit.read()){
				
				
				TokenStream  input = grammar.scanner().read(reader);  
				
				ParserTreeNode node = p.parse(input);

				for (CompilerBackEnd end : ends)
				{
					end.use(language.transform(node));
				}
			}
		}
	}
}

