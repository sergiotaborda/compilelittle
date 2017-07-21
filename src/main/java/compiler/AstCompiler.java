/**
 * 
 */
package compiler;

import compiler.lexer.Scanner;
import compiler.lexer.TokenStream;
import compiler.parser.Parser;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;

/**
 * Generates the ParserTreeNode for each unit. If the Grammar produces an AST root node it will be added to the CompiledUnit.
 * 
 */
public class AstCompiler implements Compiler {

	private Language language;
	private Parser parser;
	
	private CompilerListener listener = new CompilerListener(){

		@Override
		public void start() {
		}
		
		@Override
		public void error(CompilerMessage error) {
			System.err.println ("Compilation Error :" + error.getMessage());
		}

		@Override
		public void warn(CompilerMessage error) {
			System.out.println ("Compilation Warning :" +error.getMessage());
		}

		@Override
		public void end() {
		
		}

        @Override
        public void trace(CompilerMessage error) {
            System.out.println ("Compilation Trace :" +error.getMessage());
        }};

	public AstCompiler(Language language){
		this.language = language;
		this.parser = language.parser();
	}

	protected CompilerListener getListener(){
		return listener;
	}
	public CompilationResultSet parse(CompilationUnitSet unitSet){

		listener.start();
		Scanner scanner = language.getGrammar().scanner();

		try{
			return new CompilationResultSet( unitSet.stream().parallel().map(unit -> {
				try{
					
					TokenStream  input = scanner.read(unit);  

					ParserTreeNode node = parser.parse(input);

					AstNode root = node.getProperty("node", AstNode.class).orElse(null);

					return new CompilationResult(new CompiledUnit(unit, node, root));

				} catch (CompilationError e){
					listener.error(new CompilerMessage(e.getMessage()));
					return new CompilationResult( new CompilationError("On " + unit.getName() +" error: " + e.getMessage()));
				} catch (Exception e){
					listener.error(new CompilerMessage(e.getMessage()));
					return new CompilationResult( new RuntimeException("On " + unit.getName() +" error: " + e.getMessage() , e));
				} 
			}));
		} finally {
			listener.end();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCompilerListener(CompilerListener listener) {
		this.listener = listener;
	}
}

