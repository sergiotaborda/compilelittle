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

	protected Grammar grammar;
	protected StringBuilder builder= new StringBuilder();

	/**
	 * Constructor.
	 * @param table
	 */
	public TokenState(Grammar grammar) {
		this.grammar = grammar;
	}

	public TokenState(Grammar grammar, StringBuilder builder) {
		this.grammar = grammar;
		this.builder = builder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParseState recieve(ScanPosition pos, char c, Consumer<Token> tokensQueue) {

		Optional<Token> test = Optional.empty();

		if (grammar.isNumberStarter(c)){
			builder.append(c);
			return new NumberLiteralTokenState(this);
		} else if (grammar.isStopCharacter(c)){
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
				return new LineCommentTokenState(grammar);
			} else if (test.get().isStartMultiLineComment() ){
				return new MultiLineCommentTokenState(grammar);
			} else if (test.get().isStringLiteralStart()){
				return new StringLiteralTokenState(this);
			}else if (test.get().isNumberLiteral()){
				return new NumberLiteralTokenState(this);
			}else if (test.get().isOperator()){
				return new OperatorTokenState(this);
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
			Optional<Token> test = grammar.maybeMatch(pos, builder.toString());
			
			if (test.isPresent()){
				tokensQueue.accept(test.get());
				builder.delete(0, builder.length());
			}
		}
	}


}
