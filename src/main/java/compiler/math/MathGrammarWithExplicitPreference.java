/**
 * 
 */
package compiler.math;

import java.util.Optional;

import compiler.AbstractGrammar;
import compiler.SymbolBasedToken;
import compiler.TokenSymbol;
import compiler.lexer.ScanPosition;
import compiler.lexer.Token;
import compiler.parser.Associativity;
import compiler.parser.NonTerminal;
import compiler.parser.Numeric;
import compiler.parser.SemanticStackItem;
import compiler.parser.Terminal;
import compiler.parser.TokenPreference;
import compiler.parser.TokenStackItem;

/**
 * 
 */
public class MathGrammarWithExplicitPreference extends AbstractGrammar {


	public MathGrammarWithExplicitPreference(){
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

		NonTerminal unit = addNonTerminal(NonTerminal.of("unit"));
		NonTerminal expr = addNonTerminal(NonTerminal.of("expr"));
		NonTerminal op = addNonTerminal(NonTerminal.of("operator"));

		unit.setRule(expr);
		expr.setRule(Numeric.instance().or(expr.add(op).add(expr)).or(Terminal.of("(").add(expr).add(Terminal.of(")"))));
		op.setRule(Terminal.of("+").or(Terminal.of("-")).or(Terminal.of("*")).or(Terminal.of("/")));

		return unit;
	}
	
	protected void posInit() {
		
		setOperator(1, Associativity.RIGHT_TO_LEFT, "+", "-");
		setOperator(2, Associativity.LEFT_TO_RIGTH, "*", "/");
		
		installSemanticActions();
	}



	@Override
	public Optional<Token> maybeMatch(ScanPosition pos,String text) {
		try {
			Integer.parseInt(text);
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.LiteralWholeNumber)); 
		} catch (Exception e){
			return super.maybeMatch(pos, text);
		}
	}

	@Override
	public Optional<Token> terminalMatch(ScanPosition pos,String text){
		try {
			Integer.parseInt(text);
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.LiteralWholeNumber)); 
		} catch (Exception e){
			return super.terminalMatch(pos, text);
		}
	}

	protected void installSemanticActions() {

		getNonTerminal("unit").addSemanticAction((p,r)->{
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("operator").addSemanticAction((p,r)->{
			if ("+".equals(r.get(0).getLexicalValue())){
				p.setSemanticAttribute("node", new compiler.math.ast.SumNode());
			} else if ("*".equals(r.get(0).getLexicalValue())){
				p.setSemanticAttribute("node", new compiler.math.ast.MultiplyNode());
			}  else if ("/".equals(r.get(0).getLexicalValue())){
				p.setSemanticAttribute("node", new compiler.math.ast.DivideNode());
			}  else if ("-".equals(r.get(0).getLexicalValue())){
				p.setSemanticAttribute("node", new compiler.math.ast.DiffNode());
			}
		});

		getNonTerminal("expr").addSemanticAction((p,r)->{
			if (r.size() == 1){
				p.setAstNode(new compiler.math.ast.Value(Long.parseLong(r.get(0).getLexicalValue())));
			} else {
				if ("(".equals(r.get(0).getLexicalValue())){
					p.setSemanticAttribute("node", r.get(1).getAstNode().get());
				} else {
					compiler.math.ast.MathExpressionNode node = new compiler.math.ast.ExpressionNode();
					
					for(int i =0; i < r.size(); i++){
						node.add(r.get(i).getAstNode().get());
					}
					p.setSemanticAttribute("node", node);
				}
				
			}


		});
	}
}
