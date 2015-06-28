/**
 * 
 */
package compiler;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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
		
		long mmark = System.currentTimeMillis();
		Parser p = language.parser();
		long mtime = System.currentTimeMillis() - mmark;
		
		System.out.println("Retrive parser : " + mtime + " ms");
		
		
		unitSet.stream().parallel().forEach(unit -> {
			try ( Reader reader = unit.read()){
				
				long mark = System.currentTimeMillis();
				TokenStream  input = grammar.scanner().read(reader);  
				
				long time = System.currentTimeMillis() - mark;
				
				System.out.println("Reading Tokens : " + time + " ms");
				
				mark = System.currentTimeMillis();
				ParserTreeNode node = p.parse(input);

				time = System.currentTimeMillis() - mark;
				
				System.out.println("Parsing : " + time + " ms");
				
				for (CompilerBackEnd end : ends)
				{
					end.use(language.transform(node));
				}
			} catch (RuntimeException e){
				throw new RuntimeException("On " + unit.getName() +":" + e.getMessage() , e);
			} catch (IOException e){
				// use quueue for each unit to hold errors
			} 
		});
	}
}

