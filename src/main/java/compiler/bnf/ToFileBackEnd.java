/**
 * 
 */
package compiler.bnf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import compiler.CompiledUnit;
import compiler.CompilerBackEnd;
import compiler.syntax.AstNode;

/**
 * 
 */
public class ToFileBackEnd implements CompilerBackEnd {

	
	private File file;

	public ToFileBackEnd(File file){
		this.file = file;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void use(CompiledUnit unit) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(file))){
			
			write(unit.getAstRootNode(), writer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param root
	 * @param writer
	 */
	private void write(AstNode root, PrintWriter writer) {
	
		if (root == null){
			writer.write("null");
		} else if (root instanceof RulesList){
			for(AstNode n : root.getChildren()){
				write(n, writer);
			}
		} else if (root instanceof Rule){
			Rule rule = (Rule) root;
			writer.append(rule.getName()).append(" = ");
			write(rule.getExpression(), writer);
			writer.println(";");
		}else if (root instanceof RulesAlternative){
			boolean first = true;
			for(AstNode n : root.getChildren()){
				if (first){
					first = false;
				} else {
					writer.append("| ");
				}
				write(n, writer);
			}
		} else if (root instanceof RulesSequence){
			boolean first = true;
			for(AstNode n : root.getChildren()){
				if (first){
					first = false;
				} else {
					writer.append(", ");
				}
				write(n, writer);
			}
		} else if (root instanceof Literal){
			Literal rule = (Literal) root;
			writer.append("'").append(rule.getName()).append("' ");
		} else if (root instanceof RuleRef){
			RuleRef rule = (RuleRef) root;
			writer.append(rule.getName()).append(" ");
		} else {
			throw new RuntimeException("Not found");
		}
	}



}
