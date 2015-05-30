# compilelittle
A little parser creation library in Java

This is still in a very early development phase and at this point is meary an experiment.

The compiler supports Bottom-up grammars like LR(0), SLR(1) and LALR(1) with a GLR parallel implementation for comflits.  Precedence and association rules are not supported at this point and must be baked into the grammar
( see the sense grammar example for reference). 

## Grammar

First you create a Grammar 

```
public class ReferenceGrammar extends AbstractGrammar {

	/**
	 * 
	 * S -> A A
	 * A -> a A | b
	 */
	@Override
	protected NonTerminal defineGrammar() {
		NonTerminal S = NonTerminal.of("S");
		NonTerminal A = NonTerminal.of("A");
		
		Terminal a = Terminal.of("a");
		Terminal b = Terminal.of("b");
		S.setRule(A.add(A));
		ProductionSequence A1 = a.add(A);
		A.setRule(A1.or(b));
		return S;
	}

	@Override
	public boolean isIgnore(char c) {
		return c != 'a' && c != 'b';
	}
	
	protected void addStopCharacters(Set<Character> stopCharacters) {
		stopCharacters.add('a');
		stopCharacters.add('b');
	}
}
```

Semantic Actions are also supported. 

## Compiling
Then you use it to compile some compilation unit (file content or string)

```
		String text = "aaabb";
		
		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new StringCompilationUnit(text));

		Compiler compiler = new Compiler(new ReferenceLanguage());
		compiler.compile(unitSet);

``` 

## BNF 

A simple BNF Grammar is also supported and a ```ToJavaBackEnd``` is provided for convinience.
You can compile the grammar to an abstract class an then inherit from it to complete the rules. 

```

public class Builder {

	@Test
	public void test() throws IOException {
		
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/sense.bnf");
		File javaOut = new File(new File(".").getAbsoluteFile().getParentFile(), "src/main/java/compiler/sense/AbstractSenseGrammar.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));


		final Compiler compiler = new Compiler(new EBnfLanguage());
		compiler.addBackEnd(new ToJavaBackEnd(javaOut, "compiler.sense.AbstractSenseGrammar"));
		compiler.compile(unitSet);
	}

}
```