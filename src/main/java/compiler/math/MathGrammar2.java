/**
 * 
 */
package compiler.math;

import compiler.AbstractGrammar;
import compiler.parser.Identifier;
import compiler.parser.NonTerminal;
import compiler.parser.Numeric;
import compiler.parser.Terminal;

/**
 * 
 */
public class MathGrammar2 extends AbstractGrammar {

	
	public MathGrammar2(){
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIgnore(char c) {
		return c == '\t' || c == '\r' || c == ' ';
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected NonTerminal defineGrammar() {
		

		NonTerminal expr = NonTerminal.of("expr");
		NonTerminal nopr = NonTerminal.of("nopr");
		NonTerminal vopr = NonTerminal.of("vopr");
		NonTerminal op = NonTerminal.of("operator");
		
		expr.setRule(nopr.or(vopr).or(Terminal.of("(").add(expr).add(Terminal.of(")"))));
		nopr.setRule(Numeric.instance().add(op).add(expr));
		vopr.setRule(Identifier.instance().add(op).add(expr));
		op.setRule(Terminal.of("+").or(Terminal.of("-")).or(Terminal.of("*")).or(Terminal.of("/")));
		

		return expr;
	}


}
