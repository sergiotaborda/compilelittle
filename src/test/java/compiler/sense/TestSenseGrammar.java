/**
 * 
 */
package compiler.sense;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

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
	
	@Test @Ignore
	public void testLALR() throws IOException  {

		SenseGrammar g = new SenseGrammar();
		
		ItemStatesLookupTable table = new LALRAutomatonFactory().create().produceLookupTable(g);
		
		assertNotNull(table);
		
		try(FileWriter writer = new FileWriter(new File("./states.txt"))){
			
			writer.write(table.getStates().toString());
			writer.flush();
		//	System.out.println(table);
		}
		
		final File out = new File("./table.txt");
		writeTableToFile(table, out);
		
		SenseLookupTable stable = new SenseLookupTable(g, new FileInputStream(out));
		
		
		writeTableToFile(stable,  new File("./tableRef.txt"));
		assertEquals(table, stable);
		
	}

	private void writeTableToFile(LookupTable table,File out ) throws IOException {
		try(FileWriter writer = new FileWriter(out)){
			
			writer.append('\t');
			for( Production p  : table.columns()){
				
				writer.append(p.toString()).append('\t');
			}
			
			writer.append('\n');
			for( LookupTableRow row : table){
				
				writer.append(Integer.toString(row.getId()));
				writer.append('\t');
				
				for( Production p  : table.columns()){
					LookupTableAction action = row.getActionFor(p);
					if (action != null){
						writer.append(action.toString());
					}
					writer.append('\t');
				}

				writer.append('\n');
			}
		}
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
	public void testCompileExpression() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/expressions.sense");
		File out = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/expressions.java");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new PrintOutBackEnd());
		compiler.addBackEnd(new OutToJavaSource(out));
		compiler.compile(unitSet);

	}
	
	@Test @Ignore
	public void testCompileGenericClass() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/main/sense/collections/Array.sense");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new Compiler(new SenseLanguage());
		compiler.addBackEnd(new PrintOutBackEnd());
		compiler.compile(unitSet);

	}
	
	@Test @Ignore
	public void testCompileForEach() throws IOException {
		File file = new File(new File(".").getAbsoluteFile().getParentFile(), "src/test/resources/foreach.sense");

		ListCompilationUnitSet unitSet = new ListCompilationUnitSet();
		unitSet.add(new FileCompilationUnit(file));

 
		final Compiler compiler = new Compiler(new SenseLanguage());
		compiler.addBackEnd(new PrintOutBackEnd());
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

 
		final Compiler compiler = new Compiler(new SenseLanguage());
		compiler.addBackEnd(new PrintOutBackEnd());
		compiler.compile(unitSet);
	}
	
	@Test  
	public void testCompileLibrary() throws IOException {
		File folder = new File(new File(".").getAbsoluteFile().getParentFile(), "src/main/sense/");

		CompilationUnitSet unitSet = new FolderCompilationUnionSet(folder , name -> name.endsWith(".sense"));
	
 
		final Compiler compiler = new SenseCompiler();
		compiler.addBackEnd(new PrintOutBackEnd());
		compiler.compile(unitSet);
	}
}
