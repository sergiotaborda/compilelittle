/**
 * 
 */
package compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import compiler.lexer.EOLToken;
import compiler.lexer.ScanPosition;
import compiler.lexer.Token;
import compiler.parser.Associativity;
import compiler.parser.AutoNonTerminal;
import compiler.parser.MatchableProduction;
import compiler.parser.NonTerminal;
import compiler.parser.Production;
import compiler.parser.ProductionItem;
import compiler.parser.ProductionSequence;
import compiler.parser.ProductionTransversor;
import compiler.parser.SemanticStackItem;
import compiler.parser.TokenPreference;
import compiler.parser.TokenStackItem;

/**
 * 
 */
public abstract class AbstractGrammar extends Grammar {

	protected Set<Character> stopCharacters;
	protected Set<String> keywords;
	protected Map<String, NonTerminal> nonTerminals = new HashMap<>();
	protected Set<String> operators;
	protected List<NonTerminal> rules;
	private NonTerminal goal;

	
	protected Map<Integer, ProductionItem> finalitems = new HashMap<>();
	protected Map<KeyProductionItem, Integer > reversefinalitems = new HashMap<>();
	protected int previous =0;
	
	private Map<String, TokenPreference> preferenceMap = new HashMap<>();
	private Map<String, Associativity> associativityMap = new HashMap<>();
	
	public AbstractGrammar (){
		build();
	}

	
	protected final void setOperator(int preference, Associativity associativity, String ... terminals) {
		
		for(String s : terminals){
			preferenceMap.put(s, new TokenPreference(preference));
			associativityMap.put(s, associativity);
		}
	}
	
	public Optional<TokenPreference> getPreference(SemanticStackItem currentItem) {
		if (currentItem instanceof TokenStackItem){
			TokenStackItem t = (TokenStackItem)currentItem;
			return Optional.ofNullable(preferenceMap.get(t.getToken().getText().get()));
		} else {
			return Optional.empty();
		}
	}
	
	public Optional<Associativity> getAssociativity(SemanticStackItem currentItem) {
		if (currentItem instanceof TokenStackItem){
			TokenStackItem t = (TokenStackItem)currentItem;
			return Optional.ofNullable(associativityMap.get(t.getToken().getText().get()));
		} else {
			return Optional.empty();
		}
	}
	
	private void build(){
		goal = defineGrammar();

		init();
		
		generateTargetIds(goal);
		
	}

	/**
	 * @param goal2
	 */
	private void generateTargetIds(NonTerminal goal) {

		Queue<NonTerminal> nonterminals = new LinkedList<>();
		Set<NonTerminal> visited = new HashSet<NonTerminal>();
		nonterminals.add(goal);
		
		while (!nonterminals.isEmpty()){
			NonTerminal n = nonterminals.poll();
			visited.add(n);
			List<ProductionSequence> sequences = new ArrayList<>();
			
			findSequence(n.getRule(), sequences); 
			
			
			for (ProductionSequence s : sequences){
				ProductionItem item = ProductionItem.produceFinalFrom(n, s, previous++);
				finalitems.put(item.getId() , item);
				reversefinalitems.put(new KeyProductionItem(item) , item.getId());
				
				for (Production p : s){
					if (p.isNonTerminal() && visited.add(p.toNonTerminal())){
						nonterminals.add(p.toNonTerminal());
					}	
				}
			}
		}
		
	
	}
	

	/**
	 * @param n
	 * @return
	 */
	private void findSequence(Production rule, List<ProductionSequence> sequences) {
		if(rule.isSequence()){
			sequences.add(rule.toSequence());
		} else if (rule.isAlternative()){
			for(Production s : rule.toAlternative()){
				findSequence(s, sequences);
			}
		} else {
			sequences.add(new ProductionSequence(rule));
		}
	}

	/**
	 * @param targetId
	 * @return
	 */
	public ProductionItem getFinalProductionItem(int targetId) {
		return finalitems.get(targetId);
	}
	
	public int getFinalProductionItemTargetId(ProductionItem item) {
		Integer i = reversefinalitems.get(new KeyProductionItem(item));
		if (i == null){
			return -1;
		}
		return i.intValue();
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
	
	public boolean hasNonTerminal(String name){
		
		return nonTerminals.containsKey(name);
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

		ProductionTransversor.transverse(goal, new CharactersReaderProductionWalker(this));
		
		posInit();
	}


	protected void posInit() {

	}
	


	/**
	 * @param stopCharacters2
	 */
	protected void addStopCharacters(Set<Character> stopCharacters) {
		
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
		} else if (isStartMultilineComment(text)){
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.StartMultilineComment));
		} else if (isEndMultilineComment(text)){
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.EndMultilineComment));
		} else if (isStartInlineComent(text)){
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.StartInlineComment));
		} else if (operators.contains(text)){
			return Optional.of(new SymbolBasedToken(pos,text,  TokenSymbol.Operator));
		} else if (text.length() == 1 && stopCharacters.contains(text.charAt(0))){
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.ID));			
		} 


		return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.ID));
	}

	/**
	 * @param text
	 * @return
	 */
	protected boolean isStartInlineComent(String text) {
		return false;
	}

	/**
	 * @param text
	 * @return
	 */
	protected boolean isEndMultilineComment(String text) {
		return false;
	}

	/**
	 * @param text
	 * @return
	 */
	protected boolean isStartMultilineComment(String text) {
		return false;
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
		} else if (isVersionLiteral(text)){
			return Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.LiteralVersion));
		}
		
		TokenSymbol s = keywords.contains(text) ? TokenSymbol.KeyWord :  TokenSymbol.ID;

		return Optional.of(new SymbolBasedToken(pos, text,s));
	}


	/**
	 * @param text
	 * @return
	 */
	public boolean isVersionLiteral(String text) {
		return text.contains(".");
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
