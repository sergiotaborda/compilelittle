/**
 * 
 */
package compiler.bnf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import compiler.CompilerBackEnd;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;

/**
 * 
 */
public class ToJavaBackEnd implements CompilerBackEnd {


	private File file;
	private String className;
	private String packageName;

	public ToJavaBackEnd(File file, String qualifiedClassName){
		this.file = file;
		String[] s = qualifiedClassName.split("\\.");
		this.className = s[s.length -1];
		this.packageName = qualifiedClassName.substring(0, qualifiedClassName.length() - 1 - className.length());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void use(AstNode root) {

		root = transformOptionals(root);
		
		if (root == null){
			return;
		}

		try (PrintWriter writer = new PrintWriter(new FileWriter(file))){

			writer.println("package " +packageName + ";");

			writer.println("import compiler.AbstractGrammar;");
			writer.println("import compiler.parser.EmptyTerminal;");
			writer.println("import compiler.parser.Identifier;");
			writer.println("import compiler.parser.NonTerminal;");
			writer.println("import compiler.parser.Terminal;");
			writer.println("import compiler.parser.Text;");
			writer.println("import compiler.parser.Numeric;");
			writer.println();
			writer.println("public abstract class " + className + " extends AbstractGrammar {");
			writer.println();
			writer.println("public " + className +  " (){");
			writer.println("	super();");
			writer.println("}");
			writer.println();
			writer.println("protected NonTerminal defineGrammar() {");
			writer.println();
			write(root, writer);
			writer.println();
			
			Rule goal = (Rule)((RulesList)root).getChildren().get(0);
			
			writer.println("	return " + escapeName(goal.getName()) + ";");
			
			writer.println("}");
			writer.println();
			writer.println("}");

			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param root
	 * @return
	 */
	private AstNode transformOptionals(AstNode root) {
	
		TreeTransverser.tranverse(root, new OptionalTransformVisitorWithSubstitution());
		
		return root;
	}
	

	/**
	 * @param n
	 * @param writer
	 */
	private void writeSignature(Rule n, PrintWriter writer) {
		writer.println("NonTerminal " + escapeName(n.getName()) + " = addNonTerminal(NonTerminal.of(\""+ n.getName()  + "\"));");
	}
	
	private String parseOptional(String name){
		if (name.endsWith("?")){
			return name.substring(0, name.length()-1);
		}
		return name;
	}
	
	private String escapeName(String name){
		if (name.equals("throws") || name.equals("finally")){
			return "nt" + name;
		}
		return name;
	}
	
	/**
	 * @param root
	 * @param writer
	 */
	private void write(AstNode root, PrintWriter writer) {

		if (root instanceof RulesList){
			
			for(AstNode n : root.getChildren()){
				if (n instanceof Rule){
					
					writeSignature((Rule)n, writer);
				}
			
			}
			writer.println();
			writer.println();
			for(AstNode n : root.getChildren()){
				write(n, writer);
			}
		} else if (root instanceof Rule){
			Rule rule = (Rule) root;
//			Optional<RuleRef> template = checkMultiple(rule);
//			if (template.isPresent()){
//				writer.append(escapeName(rule.getName())).append(".setRule(");
//				write(template.get(), writer);
//				writer.print(".noneOrMore()");
//				writer.println(");");
//			} else {
				writer.append(escapeName(rule.getName())).append(".setRule(");
				write(rule.getExpression(), writer);
				writer.println(");");
			//}
			
			
			
		} else if (root instanceof RuleRef){
			RuleRef rule = (RuleRef) root;
			
			if (rule.getName().startsWith("identifier") ){
				writer.append("Identifier.instance()");
			} else if (rule.getName().equals("empty")){
				writer.append("EmptyTerminal.instance()");
			} else if (rule.getName().equals("number")){
				writer.append("Numeric.instance()");
			} else if (rule.getName().equals("text")){
				writer.append("Text.instance()");
			} else if (rule.getName().equals("characterLiteral")){
				writer.append("Text.instance()");
			} else if (rule.getName().endsWith("?")){
				
				writer.append(escapeName(parseOptional(rule.getName()))).append(".optional()");
			} else {
				writer.append(escapeName(parseOptional(rule.getName())));
			}
			
			
		} else if (root instanceof RulesAlternative){

			write(root.getChildren().get(0), writer);
			
			for (int i = 1 ; i < root.getChildren().size(); i++){
				writer.append(".or(");
				write(root.getChildren().get(i), writer);
				writer.append(")");
			}
			
		} else if (root instanceof RulesSequence){
			
			write(root.getChildren().get(0), writer);
			
			for (int i = 1 ; i < root.getChildren().size(); i++){
				writer.append(".add(");
				write(root.getChildren().get(i), writer);
				writer.append(")");
			}
		} else if (root instanceof Literal){
			Literal rule = (Literal) root;
			writer.append("Terminal.of(\"").append(escape(rule.getName())).append("\")");
		}
	}

	private CharSequence escape(String name) {
		if (name.equals("\"")){
			return "\\" + name;
		} else if (name.equals("\\")){
			return "\\" + name;
		}
		return name;
	}

	/**
	 * @param rule
	 * @return
	 */
	private Optional<RuleRef> checkMultiple(Rule rule) {
		AstNode r = rule.getExpression();
		if (r instanceof RulesAlternative){
			RulesAlternative alt = (RulesAlternative)r;
			if (alt.getChildren().size() == 2){
				AstNode template = alt.getChildren().get(0);
				AstNode s = alt.getChildren().get(1);
				if (template instanceof RuleRef && s instanceof RulesSequence){
					RulesSequence seq = (RulesSequence)s;
					if (seq.getChildren().size() == 2){
						
						AstNode original = seq.getChildren().get(0);
						AstNode repeat = seq.getChildren().get(1);
						
						if (original instanceof RuleRef && repeat instanceof RuleRef){
							RuleRef ref = (RuleRef) original;
							RuleRef templateRef = (RuleRef) template;
							RuleRef repeatRef = (RuleRef) repeat;
							
							if (ref.getName().equals(rule.getName()) || repeatRef.getName().equals(repeatRef.getName())){
								
								return Optional.of(templateRef);
								
							}
						}
						
					}
				}
			}
		} 
		
		return Optional.empty();
		
	}



}
