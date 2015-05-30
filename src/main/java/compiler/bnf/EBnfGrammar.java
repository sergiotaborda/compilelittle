/**
 * 
 */
package compiler.bnf;

import java.util.Optional;
import java.util.Set;

import compiler.AbstractGrammar;
import compiler.parser.BottomUpParser;
import compiler.parser.Identifier;
import compiler.parser.LALRAutomatonFactory;
import compiler.parser.NonTerminal;
import compiler.parser.Parser;
import compiler.parser.Text;
import compiler.syntax.AstNode;

/**
 * 
 */
public class EBnfGrammar extends AbstractGrammar {

	public EBnfGrammar (){
		super();
	}

	public Parser parser() {
		return new BottomUpParser(this, new LALRAutomatonFactory());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStringLiteralDelimiter(char c) {
		return c == '\'';
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIgnore(char c) {
		return c == '\t' || c == '\r' || c == ' ' ;
	}

	protected void addStopCharacters(Set<Character> stopCharacters) {
		stopCharacters.add(' ');
		stopCharacters.add('\'');
		stopCharacters.add(';');
		stopCharacters.add('\n');
	}


	//	 <rules>         = <rule> | <rule> , '\n', <rules>
	//	 <rule>           = <rule-name>,  "=",  <expression>, ";"
	//	 <expression>     = <terms> | <terms>, "|", <expression>
	//	 <terms>        = <term> | <term>, ",", <terms>
	//	 <term>           = <literal> | <ruleref>
	//   <rule-name>      = IDENTIFIER
	//   <ruleref>      = IDENTIFIER
	//	 <literal>        = TEXT

	protected NonTerminal defineGrammar() {
		//changed terms to continue with terms

		NonTerminal rules = NonTerminal.of("rules").addSemanticAction( (p, r) -> {
			RulesList seq = new RulesList();

			for (int i=0; i < r.size(); i+=2){
				AstNode n =(AstNode)r.get(i).getSemanticAttribute("node").get();
				if (n instanceof RulesList)
				{
					for(AstNode it : n.getChildren()){
						seq.add(it);
					}
				} else {
					seq.add(n);
				}
				
			}

			p.setSemanticAttribute("node", seq);
		} );

		NonTerminal rule = NonTerminal.of("rule").addSemanticAction( (p, r) -> {


			Rule n = new Rule();

			Optional<Object> attr = r.get(0).getSemanticAttribute("lexicalValue");

			if (!attr.isPresent()){
				p.setSemanticAttribute("node", r.get(0).getSemanticAttribute("node").get());
			} else {

				n.setName((String)attr.get());

				n.add((AstNode)r.get(2).getSemanticAttribute("node").get());

				p.setSemanticAttribute("node", n);
			}

		} );

		NonTerminal term = NonTerminal.of("term").addSemanticAction( (p, r) -> {			
			p.setSemanticAttribute("node", r.get(0).getSemanticAttribute("node").get());
		} );

		NonTerminal expression = NonTerminal.of("expression").addSemanticAction( (p, r) -> {
			RulesAlternative seq = new RulesAlternative();

			for (int i=0; i < r.size(); i+=2){
				seq.add((AstNode)r.get(i).getSemanticAttribute("node").get());
			}

			p.setSemanticAttribute("node", seq);


		} );

		NonTerminal terms = NonTerminal.of("terms").addSemanticAction( (p, r) -> {
			RulesSequence seq = new RulesSequence();

			for (int i=0; i < r.size(); i+=2){
				AstNode n = (AstNode)r.get(i).getSemanticAttribute("node").get();
				if (n instanceof RulesSequence)
				{
					for(AstNode it : n.getChildren()){
						seq.add(it);
					}
				} else {
					seq.add(n);
				}
			}

			p.setSemanticAttribute("node", seq);
		} );

		NonTerminal rulename = NonTerminal.of("rulename").addSemanticAction( (p, r) -> {
			Rule n = new Rule();
			n.setName((String)r.get(0).getSemanticAttribute("lexicalValue").get());
			p.setSemanticAttribute("node", n);
		} );

		NonTerminal ruleref = NonTerminal.of("ruleref").addSemanticAction( (p, r) -> {
			RuleRef n = new RuleRef();
			n.setName((String)r.get(0).getSemanticAttribute("lexicalValue").get());
			p.setSemanticAttribute("node", n);
		} );

		NonTerminal literal = NonTerminal.of("literal").addSemanticAction( (p, r) -> {
			Literal n = new Literal();
			n.setName((String)r.get(0).getSemanticAttribute("lexicalValue").get());
			p.setSemanticAttribute("node", n);
		} );


		term.setRule(ruleref.or(literal));


		rules.setRule(rule.or(rule.add("\n").add(rules).label("rulesList")));
		terms.setRule(term.or(term.add(",").add(terms).label("list")));
		expression.setRule(terms.or(terms.add("|").add(expression).label("alt")));
		rulename.setRule(Identifier.instance());
		rule.setRule(rulename.add("=").add(expression).add(";"));
		ruleref.setRule(Identifier.instance());
		literal.setRule(Text.instance());

		return rules;
	}





}
