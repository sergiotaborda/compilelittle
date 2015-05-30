/**
 * 
 */
package compiler.math;

import compiler.AbstractGrammar;
import compiler.parser.EmptyTerminal;
import compiler.parser.Identifier;
import compiler.parser.NonTerminal;
import compiler.parser.Numeric;
import compiler.parser.Terminal;

/**
 * 
 */
public class MathGrammar extends AbstractGrammar {

	
	public MathGrammar(){
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
		
		NonTerminal goal = NonTerminal.of("goal");
		NonTerminal expr = NonTerminal.of("expr");
		NonTerminal term = NonTerminal.of("term");
		term.addSemanticAction((a, s) -> System.out.println("Semantic Action = " + a));
		NonTerminal factor = NonTerminal.of("factor");
		NonTerminal expr1 = NonTerminal.of("expr1");
		NonTerminal term1 = NonTerminal.of("term1");
		NonTerminal sum = NonTerminal.of("sum");
		NonTerminal diff = NonTerminal.of("diff");
		
		goal.setRule(expr);
		
		expr.setRule(term.add(expr1));
		term.setRule(factor.add(term1));
		sum.setRule(Terminal.of("+").add(term).add(expr1));
		diff.setRule(Terminal.of("-").add(term).add(expr1));
		
		expr1.setRule(sum.or(diff).or(EmptyTerminal.instance()) );
		term1.setRule((Terminal.of("*").add(factor).add(term1)).or(Terminal.of("/").add(factor).add(term1)).or(EmptyTerminal.instance()));
		factor.setRule(Numeric.instance().or( Identifier.instance()).or(Terminal.of("(").add(expr).add(Terminal.of(")"))));
		
		return goal;
	}


}
