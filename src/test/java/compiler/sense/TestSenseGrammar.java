/**
 * 
 */
package compiler.sense;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;

import compiler.CompilationUnitSet;
import compiler.Compiler;
import compiler.FileCompilationUnit;
import compiler.FirstFollowTable;
import compiler.FirstFollowTableCalculator;
import compiler.FolderCompilationUnionSet;
import compiler.PrintOutBackEnd;
import compiler.PromisseSet;
import compiler.RealizedPromisseSet;
import compiler.lexer.ListCompilationUnitSet;
import compiler.parser.Identifier;
import compiler.parser.ItemStatesLookupTable;
import compiler.parser.LALRAutomatonFactory;
import compiler.parser.LRZeroAutomatonFactory;
import compiler.parser.LookupTable;
import compiler.parser.LookupTableAction;
import compiler.parser.LookupTableRow;
import compiler.parser.MatchableProduction;
import compiler.parser.NonTerminal;
import compiler.parser.Numeric;
import compiler.parser.Production;
import compiler.parser.SLRAutomatonFactory;
import compiler.parser.SplitAction;
import compiler.parser.Terminal;
import compiler.parser.Text;
import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.Method;
import compiler.typesystem.MethodParameter;
import compiler.typesystem.MethodSignature;
import compiler.typesystem.TypeDefinition;

/**
 * 
 */
public class TestSenseGrammar {

	

	@Test @Ignore
	public void testFirstAndFollow()  {
	
		SenseGrammar g = new SenseGrammar();
		
		FirstFollowTable firstFollowTable = new FirstFollowTableCalculator().calculateFrom(g.getStartProduction());
		
		assertFalse(firstFollowTable.followOf(new NonTerminal("packageDeclaration")).isEmpty());
		assertFalse(firstFollowTable.followOf(new NonTerminal("superDeclaration")).isEmpty());
		assertFalse(firstFollowTable.followOf(new NonTerminal("blockStatement")).isEmpty());
		
		assertFalse(firstFollowTable.firstOf(new NonTerminal("typeDeclaration")).isEmpty());
		assertFalse(firstFollowTable.firstOf(new NonTerminal("type")).isEmpty());
		assertFalse(firstFollowTable.firstOf(new NonTerminal("localVariableDeclaration")).isEmpty());
		assertFalse(firstFollowTable.firstOf(new NonTerminal("whileStatement")).isEmpty());

		assertFalse(firstFollowTable.firstOf(new NonTerminal("blockStatement")).isEmpty());

		
		final PromisseSet<MatchableProduction> firstOf = new RealizedPromisseSet<MatchableProduction>(
				Identifier.instance(),
				Text.instance(),
				Numeric.instance(),
				Terminal.of("this"),
				Terminal.of("super"),
				Terminal.of("null"),
				Terminal.of("new"),
				Terminal.of("true"),
				Terminal.of("false"),
				Terminal.of("("),
				Terminal.of("++"),
				Terminal.of("--"),
				Terminal.of("+"),
				Terminal.of("-"),
				Terminal.of("~"),
				Terminal.of("!")
		);
		
		assertEquals(firstOf , firstFollowTable.firstOf(new NonTerminal("expression")));
		
		final PromisseSet<MatchableProduction> followOf = new RealizedPromisseSet<MatchableProduction>(
				Terminal.of(")"), // grouping , if, while, other control structures
				Terminal.of("]"), // indexed access
				Terminal.of(":"), // ternary
				Terminal.of(";"), // inicialization
				Terminal.of(",") // argument list
		);
		
		assertEquals(followOf , firstFollowTable.followOf(new NonTerminal("expression")));
		
		
	}
	


	
	

	@Test  @Ignore
	public void testIsNotLRZero() {

		SenseGrammar g = new SenseGrammar();
		
		LookupTable table = new LRZeroAutomatonFactory().create().produceLookupTable(g);
		
		assertNotNull(table);
		
		int conflit = 0;
		for (LookupTableRow r : table){
			for (Map.Entry<Production, LookupTableAction> cell : r){
				LookupTableAction action = cell.getValue();
				if (action instanceof SplitAction){
					//System.out.println("Conflict at state " + r.toString() + " when next is " + cell.getKey().toString());  
					conflit++;
				}
			}
		}
		
		assertFalse(0 == conflit);
		
	}
	
	@Test @Ignore
	public void testIsNotSLROne() {

		SenseGrammar g = new SenseGrammar();
		
		LookupTable table = new SLRAutomatonFactory().create().produceLookupTable(g);
		
		assertNotNull(table);
		
		int conflit = 0;
		for (LookupTableRow r : table){
			for (Map.Entry<Production, LookupTableAction> cell : r){
				LookupTableAction action = cell.getValue();
				if (action instanceof SplitAction){
					//System.out.println("Conflict at state " + r.toString() + " when next is " + cell.getKey().toString());  
					conflit++;
				}
			}
		}
		

		
		assertFalse(0 == conflit);
		
	}
	@Test
	public void testLambda() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/lambda.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/lambda.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new PrintOutBackEnd());
	//	compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);

	}
	
	@Test
	public void testStringInterpolation() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/interpolation.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/interpolation.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new PrintOutBackEnd());
		compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);

	}
	
	@Test 
	public void testMaybeAssingment() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/maybeTest.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/maybeTest.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new PrintOutBackEnd());
		compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);

	}
	
	@Test 
	public void testVariance (){
		final SenseTypeSystem instance = SenseTypeSystem.getInstance();
		TypeDefinition maybeAny = instance.specify(SenseTypeSystem.Maybe()  , SenseTypeSystem.Any());
		TypeDefinition none = SenseTypeSystem.None();
		TypeDefinition maybeWhole = instance.specify(SenseTypeSystem.Maybe()  , SenseTypeSystem.Whole());
		TypeDefinition maybeNatural = instance.specify(SenseTypeSystem.Maybe()  , SenseTypeSystem.Natural());
		
		assertTrue( instance.isAssignableTo(maybeNatural, maybeAny));
		assertFalse(instance.isAssignableTo(maybeAny,maybeNatural));
		
		assertTrue( instance.isAssignableTo(maybeNatural, maybeWhole));
		assertFalse(instance.isAssignableTo(maybeWhole,maybeNatural));
		
		assertTrue( instance.isAssignableTo(none, maybeAny));
		assertTrue( instance.isAssignableTo(none, maybeWhole));
		assertTrue( instance.isAssignableTo(none, maybeNatural));
		
	}
	
	@Test 
	public void testIllegalSpecification (){
		// TODO should not be possible to create a maybe of a maybe
		final SenseTypeSystem instance = SenseTypeSystem.getInstance();
		instance.specify(SenseTypeSystem.Maybe()  , SenseTypeSystem.None());
	}
	
	@Test 
	public void testField() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/field.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/field.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new PrintOutBackEnd());
		compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);

	}
	
	
	@Test 
	public void testCompileExpression() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/expressions.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/expressions.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);

	}
	@Test 
	public void testCompileNativeClass() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/main/sense/collections/Array.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/main/sense/collections/Array.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);

	}
	
	@Test 
	public void testCompileGenericClass() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/main/sense/collections/Sequence.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/main/sense/collections/Sequence.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);

	}
	
//	@Test 
//	public void testSenseType() throws IOException {
//		 
//		TypeDefinition sMaybe = SenseTypeSystem.getInstance().specify(SenseTypeSystem.Maybe(), SenseTypeSystem.String());
//				
//		MethodSignature mapSignature = new MethodSignature(
//				sMaybe, 
//				"map", 
//				new MethodParameter(SenseType.Function1.of(SenseType.Natural, SenseType.String), "it")
//		);
//		
//		Optional<Method> mapMethod = sMaybe.getAppropriateMethod(mapSignature);
//		
//		SenseType binded = (SenseType) mapMethod.get().bindGenerics(mapSignature);
//		
//		Method m = binded.getAppropriateMethod(mapSignature).get();
//		
//		assertNotNull(m);
//		assertEquals(SenseTypeSystem.getInstance().specify(SenseTypeSystem.Maybe(), SenseTypeSystem.Natural()), m.getReturningType().getUpperbound());
//	}
	
	@Test 
	public void testVoid() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/void.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/void.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);

	}
	
	
	@Test 
	public void testCompilerProgram() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/program.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/program.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new PrintOutBackEnd());
		compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);
	}
	
	@Test  
	public void testCompilerInterface() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/Comparable.sense");
		
		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new PrintOutBackEnd());
		compiler.compile(unitSet);
	}
	
	@Test  @Ignore
	public void testCompileLibrary() throws IOException {
		File folder = new File(new File(".").getAbsoluteFile().getParentFile(), "src/main/sense/");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "compiled");

		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new OutToJavaSource(out));

		CompilationUnitSet unitSet = new FolderCompilationUnionSet(folder , name -> name.endsWith(".sense"));
	

		compiler.compile(unitSet);
	}


}
