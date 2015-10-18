/**
 * 
 */
package compiler.lexer;

import java.util.Optional;
import java.util.function.Consumer;

import compiler.AbstractTokenState;
import compiler.Grammar;


/**
 * 
 */
public class TokenState extends AbstractTokenState implements ParseState {

	protected StringBuilder builder= new StringBuilder();
	private Scanner scanner;

	/**
	 * Constructor.
	 * @param table
	 */
	public TokenState(Scanner scanner) {
		this.scanner = scanner;
	}

	public TokenState(Scanner scanner, StringBuilder builder) {
		this(scanner);
		this.builder = builder;
	}
	
	public Scanner getScanner(){
		return scanner;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParseState receive(ScanPosition pos, char c, Consumer<Token> tokensQueue) {

		Optional<Token> test = Optional.empty();

		Grammar grammar = this.scanner.getGrammar();
		
		if (grammar.isNumberStarter(c)){
			builder.append(c);
			return scanner.getNumberLiteralTokenState(this);
		} else if (c == 0 || grammar.isStopCharacter(c)){
			if (builder.length() > 0){

				test = grammar.terminalMatch(pos, builder.toString());

				if (test.isPresent()){
					tokensQueue.accept(test.get());
					builder.delete(0, builder.length());
					if (grammar.isIgnore(c)){
						return this;
					} else {
						builder.append(c);
					}
				} else {
					if (builder.toString().trim().length() == 0){
						builder.delete(0, builder.length());
					} else {
						throw new RuntimeException("ERRO");
					}
					
				}
			}
			else {
				if (!grammar.isIgnore(c)){
					builder.append(c);
				} else {
					return this;
				}
			}
		} else {
			if (!grammar.isIgnore(c)){
				builder.append(c);
			} else {
				return this;
			}
	
		}
		
		test = grammar.maybeMatch(pos, builder.toString());

		if (test.map( t -> t.isId() ).orElse(false)){
			test = Optional.empty();
		}

		if (test.isPresent()){
			if (test.get().isStartLineComment() ){
				return scanner.getLineCommentTokenState(this);
			} else if (test.get().isStartMultiLineComment() ){
				return scanner.getMultiLineCommentTokenState(this);
			} else if (test.get().isStringLiteralStart()){
				return scanner.getStringLiteralTokenState(this); 
			}else if (test.get().isNumberLiteral()){
				return scanner.getNumberLiteralTokenState(this);
			}else if (test.get().isOperator()){
				return scanner.getOperatorTokenState(this);
			} else {
				tokensQueue.accept(test.get());
				builder.delete(0, builder.length());
			}
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear(ScanPosition pos, Consumer<Token> tokensQueue) {
		if (builder.length() > 0){
			Optional<Token> test = scanner.getGrammar().maybeMatch(pos, builder.toString());
			
			if (test.isPresent()){
				tokensQueue.accept(test.get());
				builder.delete(0, builder.length());
			}
		}
	}


}
