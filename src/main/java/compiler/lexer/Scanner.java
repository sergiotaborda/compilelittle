/**
 * 
 */
package compiler.lexer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import compiler.CompilationUnit;
import compiler.Grammar;

/**
 * 
 */
public class Scanner  {

	protected Grammar grammar;

	public Scanner(Grammar grammar){
		this.grammar = grammar;
	}

	public Grammar getGrammar(){
		return grammar;
	}

	public final TokenStream read (CompilationUnit unit) throws IOException{
		int c;

		ParseState state = newInitialState();

		final List<Token> input = new ArrayList<>(50);

		Consumer<Token> consumer = t -> input.add(t);

		ScanPosition pos = new ScanPosition(unit);
		try ( Reader reader = unit.read()){

			while ( (c = reader.read()) > -1 ){
				pos.incrementColumn();
				state = state.receive(pos, (char)c,consumer);

				if (c == '\n'){
					pos.incrementLine();
				}
			}
		}
		state.clear(pos, consumer);
		consumer.accept(new EOFToken());

		return new ListTokenStream(input);
	}

	public ParseState newInitialState(){
		return new TokenState(this);
	}

	/**
	 * @return
	 */
	public ParseState getLineCommentTokenState(TokenState tokenState) {
		return new LineCommentTokenState(tokenState);
	}


	/**
	 * @return
	 */
	public ParseState getMultiLineCommentTokenState(TokenState tokenState) {
		return new MultiLineCommentTokenState(tokenState);
	}


	/**
	 * @param tokenState
	 * @return
	 */
	public ParseState getStringLiteralTokenState(TokenState tokenState) {
		return new StringLiteralTokenState(tokenState);
	}


	/**
	 * @param tokenState
	 * @return
	 */
	public ParseState getNumberLiteralTokenState(TokenState tokenState) {
		return new NumberLiteralTokenState(tokenState);
	}


	/**
	 * @param tokenState
	 * @return
	 */
	public ParseState getOperatorTokenState(TokenState tokenState) {
		return new OperatorTokenState(tokenState);
	}

	/**
	 * @param numberLiteralTokenState
	 * @return
	 */
	public ParseState getVersionLiteralTokenState(TokenState tokenState) {
		return new VersionLiteralTokenState(tokenState);
	}

}
