/**
 * 
 */
package compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import compiler.lexer.EOLToken;
import compiler.lexer.ScanPosition;
import compiler.lexer.Token;
import compiler.parser.AutoNonTerminal;
import compiler.parser.MatchableProduction;
import compiler.parser.NonTerminal;
import compiler.parser.Production;
import compiler.parser.ProductionAlternative;
import compiler.parser.ProductionSequence;
import compiler.parser.ProductionTransversor;


/**
 * 
 */
public abstract class AbstractGrammar extends Grammar {

	protected Set<Character> stopCharacters;
	protected Set<String> keywords;
	protected Map<String, NonTerminal> nonTerminals = new HashMap<String, NonTerminal>();
	protected Set<String> operators;
	protected List<NonTerminal> rules;
	private NonTerminal goal;


	public AbstractGrammar (){
		build();
	}

	private void build(){
		goal = defineGrammar();

		init();
	}

	
	public NonTerminal addNonTerminal(NonTerminal it){
		
		nonTerminals.put(it.getName(), it);
		return it;
		
	}
	
	public NonTerminal getNonTerminal(String name){
		
		NonTerminal a = nonTerminals.get(name);
		if (a==null){
			return new NonTerminal(name);
		}
		return a;
	}
	
	public Collection<NonTerminal> getNonTerminals(){
		
		return nonTerminals.values();
	}

	protected abstract NonTerminal defineGrammar();


	protected boolean isAlphaNumeric(char c){
		return Character.isAlphabetic(c) || Character.isDigit(c);
	}
	
	
	/**
	 * 
	 */
	private void init() {

		stopCharacters = new HashSet<>();
		
		addStopCharacters(stopCharacters);
		
		keywords = new HashSet<>();
		operators = new HashSet<>();
		rules = new ArrayList<>();

//		if (isLeftRecursive(goal, goal)){
//			throw new RuntimeException("Grammar is left Recursive");
//			//removeLeftRecursivity(queue);
//		}

		ProductionTransversor.transverse(goal, new CharactersReaderProductionWalker(this));
		
		posInit();
	}


	protected void posInit() {

	}
	

	/**
	 * @param p
	 * @param n
	 */
	private void removeLeftRecursive(Production p, NonTerminal n, List<NonTerminal> newRules) {

		if (p.isSequence()){
			ProductionSequence s = (ProductionSequence)p;
			if (s.get(0) != n){
				return;
			}
			
			s.remove(0);

		} else if (p.isAlternative()){
			ProductionAlternative a = (ProductionAlternative)p;
			
			NonTerminal rightRecursive = new NonTerminal(n.getName() + "'");
			
			Production independent= null;
			for(Production pd : a){
				if (pd.isSequence()){
					ProductionSequence spd = (ProductionSequence)pd;
					if (spd.get(0) != n){
						continue;
					}
					spd.remove(0);
					spd.add(rightRecursive);
				} else {
					if (independent == null){
						// independent
						independent = pd.add(rightRecursive);
					}
				}
			}
			
			n.setRule(independent);
			rightRecursive.setRule(a);
			
			newRules.add(rightRecursive);
		}
		
	}

	/**
	 * @param stopCharacters2
	 */
	protected void addStopCharacters(Set<Character> stopCharacters) {
		
	}

	private boolean isLeftRecursive(Production p, Production test) {

		if (p.isNonTerminal()){

			NonTerminal n = (NonTerminal)p;

			Production rule = n.getRule();

			return isLeftRecursive(rule, p);
		} else if (p.isAlternative()){
			ProductionAlternative a = (ProductionAlternative)p;

			for (Production i : a){
				if (isLeftRecursive(i, test)){
					return true;
				}
			}

			return false;

		} else if (p.isSequence()){

			ProductionSequence s = (ProductionSequence)p;
			return s.get(0).equals(test);

		} 
		return false;

	}

	/**
	 * @param queue
	 */
	private void removeLeftRecursivity(Queue<Production> queue) {

		// todo

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isStopCharacter(char c) {
		return isIgnore(c) || stopCharacters.contains(c) || isStringLiteralDelimiter(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Token> maybeMatch(ScanPosition pos,String text) {

		if (text.length() == 1 && isStringLiteralDelimiter(text.charAt(0))){
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.LiteralStringStart));
		}  else if (operators.contains(text)){
			return Optional.of(new SymbolBasedToken(pos,text,  TokenSymbol.Operator));
		} else if (text.length() == 1 && stopCharacters.contains(text.charAt(0))){
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.ID));			
		} 


		return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.ID));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Token> terminalMatch(ScanPosition pos,String text) {

		if (operators.contains(text)){
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.Operator));
		} else if (text.equals("\n")){
			return Optional.of(new EOLToken(pos));
		}
		
		TokenSymbol s = keywords.contains(text) ? TokenSymbol.KeyWord :  TokenSymbol.ID;

		return Optional.of(new SymbolBasedToken(pos, text,s));
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStringLiteralDelimiter(char c) {
		return c == '"';
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Token> stringLiteralMath(ScanPosition pos,String text) {
		return Optional.of(new SymbolBasedToken(pos, text, TokenSymbol.LiteralString));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Production getStartProduction() {
		return goal;
	}

	/**
	 * @param n
	 * @return
	 */
	public PromisseSet<MatchableProduction> getFirstSet(AutoNonTerminal n) {
		
		RealizedPromisseSet<MatchableProduction> first = new RealizedPromisseSet<>();
		first.add(n);
		return first;
	}
}
