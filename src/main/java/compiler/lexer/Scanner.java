/**
 * 
 */
package compiler.lexer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import compiler.Grammar;

/**
 * 
 */
public class Scanner  {

    Grammar grammar;
    
	public Scanner(Grammar grammar){
		this.grammar = grammar;
	}
	
	
	public TokenStream read (Reader reader) throws IOException{
		int c;
		
		ParseState state = new TokenState(grammar);

		final List<Token> input = new ArrayList<>(50);

		Consumer<Token> consumer = t -> input.add(t);
		
		ScanPosition pos = new ScanPosition();
		while ( (c = reader.read()) > -1 ){
			pos.incrementColumn();
			state = state.receive(pos, (char)c,consumer);
		
			if (c == '\n'){
				pos.incrementLine();
			}
		}
		
		state.clear(pos, consumer);
		consumer.accept(new EOFToken());
	
		return new ListTokenStream(input);
	}

}
