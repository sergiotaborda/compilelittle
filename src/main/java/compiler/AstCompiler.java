/**
 * 
 */
package compiler;

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
		
		}};

	public AstCompiler(Language language){
		this.language = language;
	}

	protected CompilerListener getListener(){
		return listener;
	}
	public CompilationResultSet parse(CompilationUnitSet unitSet){

		listener.start();
		Grammar grammar = language.getGrammar();

		Parser p = language.parser();

		try{
			return new CompilationResultSet( unitSet.stream().parallel().map(unit -> {
				try{

					TokenStream  input = grammar.scanner().read(unit);  

					ParserTreeNode node = p.parse(input);

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

