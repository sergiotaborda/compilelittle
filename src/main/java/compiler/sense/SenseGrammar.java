/**
 * 
 */
package compiler.sense;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import compiler.SymbolBasedToken;
import compiler.TokenSymbol;
import compiler.lexer.ScanPosition;
import compiler.lexer.Token;
import compiler.parser.IdentifierNode;
import compiler.parser.LookupTable;
import compiler.parser.ProductionItem;
import compiler.parser.SemanticAction;
import compiler.parser.Symbol;
import compiler.sense.typesystem.Type;
import compiler.syntax.AstNode;


/**
 * 
 */
public class SenseGrammar extends AbstractSenseGrammar{


	public SenseGrammar (){
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIgnore(char c) {
		return c == '\t' || c == '\r' ||  c== ' ' || c == '\n' ;
	}

	protected boolean isStartInlineComent(String text) {
		return "//".equals(text);
	}

	protected boolean isStartMultilineComment(String text) {
		return "/*".equals(text);
	}

	protected boolean isEndMultilineComment(String text) {
		return "*/".equals(text);
	}


	protected void addStopCharacters(Set<Character> stopCharacters) {
		stopCharacters.add('"');
		stopCharacters.add('(');
		stopCharacters.add(')');
		stopCharacters.add('{');
		stopCharacters.add('}');
		stopCharacters.add('[');
		stopCharacters.add(']');
		stopCharacters.add(';');
		stopCharacters.add(',');
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
	public Optional<Token> terminalMatch(ScanPosition pos,String text) {
		if (text.trim().length() == 0){
			return Optional.empty();
		}
		if (text.length() > 1 && text.startsWith("\"") && text.endsWith("\"")){
			return  Optional.of(new SymbolBasedToken(pos, text.substring(1,text.length()-1), TokenSymbol.LiteralString));
		} else if (text.matches("^\\d+[NILDFMJQ]?$")){
			return  Optional.of(new SymbolBasedToken(pos, text, TokenSymbol.LiteralWholeNumber));
		} else if (text.matches("[0-9]+\\.?[0-9]+([eE][-+]?[0-9]+)?[NILDFMJQ]?")){
			return  Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.LiteralDecimalNumber));
		} 

		return super.terminalMatch(pos, text);
	}

	public boolean isDigit(char c) {
		return isNumberStarter(c) || c == '0' || c == '_' || c == '.' || c == 'e' || c=='E' || c == 'x' 
				|| c == 'N'	|| c == 'I' || c == 'L' || c == 'D' || c == 'F'  || c == 'M' || c == 'J' || c == 'Q' ;
	}

	/**
	 * @param text
	 * @return
	 */
	private Type determineType(String literalNumber) {
		char end = literalNumber.charAt(literalNumber.length()- 1);
		if (literalNumber.contains(".") && literalNumber.matches("[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?[NILDFMJQ]?")){
			// decimal
			if (Character.isDigit(end) || end == 'M'){
				return Type.Decimal;
			} else if (end == 'N'){
				throw new SyntaxError("A decimal number cannot end with N");
			} else if (end == 'I'){
				throw new SyntaxError("A decimal number cannot end with I");
			} else if (end == 'L'){
				throw new SyntaxError("A decimal number cannot end with L");
			} else if (end == 'D'){
				return Type.Double;
			}else if (end == 'F'){
				return Type.Float;
			}else if (end == 'J'){
				return Type.Imaginary;
			}else if (end == 'Q'){
				throw new SyntaxError("A decimal number cannot end with Q. Use n.over(m) or the n\\m notation");
			} 
		} else if (literalNumber.matches("^\\d+[NILDFMJQ]?$")){
			// whole
			if (Character.isDigit(end) || end == 'N'){
				return Type.Natural;
			} else if (end == 'I'){
				return Type.Int;
			} else if (end == 'L'){
				return Type.Long;
			} else if (end == 'D'){
				return Type.Double;
			}else if (end == 'F'){
				return Type.Float;
			}else if (end == 'M'){
				return Type.Decimal;
			}else if (end == 'J'){
				return Type.Imaginary;
			}else if (end == 'Q'){
				return Type.Rational;
			} 
		} 

		throw new SyntaxError("'" + literalNumber + "' is not reconized as a Number");

	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Token> stringLiteralMath(ScanPosition pos,String text) {
		return Optional.of(new SymbolBasedToken(pos, text, TokenSymbol.LiteralString));
	}

	protected void posInit() {

		this.keywords.add("/**");
		this.keywords.add("//");
		this.keywords.add("*/");

		installSemanticActions();
	}

	protected void installSemanticActions() {

		getNonTerminal("qualifiedName").addSemanticAction((p,r)->{

			QualifiedNameNode name = new QualifiedNameNode();

			for (int i = 0; i< r.size(); i+=2){
				if (r.get(i).getAstNode().isPresent()){
					AstNode node = r.get(i).getAstNode().get();
					if (node instanceof QualifiedNameNode){
						name =  (QualifiedNameNode)node;
					} else if (node instanceof IdentifierNode){
						name.append(((IdentifierNode)node).getId());
					}

				} else {
					name.append((String)r.get(i).getSemanticAttribute("lexicalValue").get());
				}
			}

			p.setSemanticAttribute("node", name);
		});

		getNonTerminal("unit").addSemanticAction( (p, r) -> {

			
			UnitTypes types;
			if (r.size() == 1){
				p.setSemanticAttribute("node",r.get(0).getSemanticAttribute("node").get());
			} else if (r.size() == 3){
				QualifiedNameNode packageName = ensureQualifiedName(r.get(0).getAstNode().get());

				
				AstNode node = (AstNode) r.get(2).getSemanticAttribute("node").get();
				if (node instanceof ClassType){
					types = new UnitTypes();
			
					ClassType type = (ClassType)node;
					
					type.setName(packageName.getName() + "." + type.getName());
			
					types.add(type);

				} else {
					types = (UnitTypes)node;
					
					
					for (AstNode a : types.getChildren()){
						ClassType type = (ClassType)a;
				
						type.setName(packageName.getName() + "." + type.getName());
					}

				}

				ImportsNode imports= new ImportsNode();
				if (r.get(1).getSemanticAttribute("node").isPresent()){
					 imports = (ImportsNode) r.get(1).getSemanticAttribute("node").get();
					 
				}
				types.add(imports);
				p.setSemanticAttribute("node", types);

			} else {
				types = new UnitTypes();
				p.setSemanticAttribute("node", types);
			}



		});


		getNonTerminal("maybeImportDeclarations").addSemanticAction( (p, r) -> {

			if (r.get(0).getAstNode().get() instanceof ImportsNode){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				ImportsNode in = new ImportsNode();
				in.add(r.get(0).getAstNode().get());
				p.setAstNode(in);
			}


		});

		getNonTerminal("importDeclarations").addSemanticAction( (p, r) -> {
			if (r.size() == 1 ){
				if (r.get(0).getAstNode().get() instanceof ImportsNode){
					p.setAstNode(r.get(0).getAstNode().get());
				} else {
					ImportsNode in = new ImportsNode();
					in.add(r.get(0).getAstNode().get());
					p.setAstNode(in);
				}

			} else {
				AstNode node = r.get(0).getAstNode().get();
				ImportsNode in;
				if (node instanceof ImportsNode){
					in = (ImportsNode)node;
					in.add(r.get(1).getAstNode().get());
				} else {
					in = new ImportsNode();
					in.add(r.get(0).getAstNode().get());
					in.add(r.get(1).getAstNode().get());
				}

				p.setAstNode(in);
			}


		});

		getNonTerminal("importDeclaration").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				ImportsNode q = new ImportsNode();
				q.add((AstNode) r.get(0).getSemanticAttribute("node").get());	
				p.setSemanticAttribute("node", q);
			} else {
				ImportNode q = new ImportNode();
				q.setName(r.get(1).getAstNode(QualifiedNameNode.class).get());
				p.setSemanticAttribute("node", q);
			}

		});

		getNonTerminal("packageDeclaration").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(1).getAstNode().get());
		});

		getNonTerminal("importDeclarations");
		getNonTerminal("importDeclaration");
		getNonTerminal("singleTypeImportDeclaration");
		getNonTerminal("typeImportOnDemandDeclaration");

		getNonTerminal("typeDeclarations").addSemanticAction( (p, r) -> {

			UnitTypes types = new UnitTypes();

			for (Symbol s : r){
				if (s.getSemanticAttribute("node").isPresent()){
					types.add((AstNode)s.getSemanticAttribute("node").get());
				}
			}
			p.setSemanticAttribute("node",types);

		});

		getNonTerminal("typeDeclaration").addSemanticAction( (p, r) -> {
			p.setSemanticAttribute("node",r.get(0).getSemanticAttribute("node").get());
		});

		getNonTerminal("annotations").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				AstNode node = r.get(0).getAstNode().get();
				if (node instanceof AnnotationListNode){
					p.setAstNode(node);
				} else {
					AnnotationListNode list = new AnnotationListNode();
					list.add(node);
					p.setAstNode(list);
				}
			} else {
				AnnotationListNode node;

				if (r.get(0).getAstNode().get() instanceof AnnotationListNode) {
					node = r.get(0).getAstNode(AnnotationListNode.class).get();
					node.add(r.get(1).getAstNode().get());
				} else {
					node = new AnnotationListNode();
					node.add(r.get(0).getAstNode().get());
					node.add(r.get(1).getAstNode().get());
				}

				p.setAstNode(node);
			}

		});

		getNonTerminal("annotation").addSemanticAction( (p, r) -> {

			AnnotationNode node = new AnnotationNode();

			Symbol symbol = r.get(0);
			String op;
			if (symbol.getSemanticAttribute("lexicalValue").isPresent()){
				op = (String)symbol.getSemanticAttribute("lexicalValue").get();
			} else {
				op = (String)((Symbol)symbol.getParserTreeNode().getChildren().get(0)).getSemanticAttribute("lexicalValue").get();
			}

			node.setName(op);
			p.setAstNode(node);


		});

		getNonTerminal("classDeclaration").addSemanticAction( (p, r) -> {

			if (r.size() == 1){	
				p.setAstNode(r.get(0).getAstNode().get());
			} else {

				ClassType n = new ClassType();

				Optional<AnnotationListNode> annots = r.get(0).getAstNode(AnnotationListNode.class);

				if (annots.isPresent()){
					n.setAnnotationList(annots.get());
				}
				n.setName((String)r.get(2).getSemanticAttribute("lexicalValue").get());

				if (r.get(3).getAstNode().isPresent()){
					AstNode node = r.get(3).getAstNode().get();
					ParametricTypesNode ptn;
					if (node instanceof TypeNode){
						ptn = new ParametricTypesNode();
						ptn.add(node);

					} else {
						ptn = (ParametricTypesNode) node;
					}
					n.setGenerics(ptn);

				}

				Optional<AstNode> ext = r.get(4).getAstNode();

				if (ext.isPresent()){
					n.setSuperType(ensureTypeNode(ext.get()));
				}

				AstNode b = r.get(5).getAstNode().get();

				if (b instanceof ClassBodyNode){
					n.setBody((ClassBodyNode)b);
				} else {
					ClassBodyNode c = new ClassBodyNode();
					c.add(b);
					n.setBody(c);
				}

				p.setSemanticAttribute("node", n);
			}

		});


		getNonTerminal("genericTypesDeclaration").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				p.setAstNode(r.get(1).getAstNode().get());
			}

		});

		getNonTerminal("classBody").addSemanticAction( (p, r) -> {
			if (r.size() == 2){
				p.setAstNode(new ClassBodyNode());
			} else {
				p.setAstNode(r.get(1).getAstNode().get());
			}

		});

		getNonTerminal("classBodyDeclarations").addSemanticAction( (p, r) -> {

			ClassBodyNode body = new ClassBodyNode();
			AstNode node = r.get(0).getAstNode().get();
			if ( node instanceof ClassBodyNode){
				body = (ClassBodyNode)node;

				body.add(r.get(1).getAstNode().get());
			} else {
				for(Symbol s : r){
					body.add(s.getAstNode().get());
				}
			}
			p.setAstNode(body);

		});

		getNonTerminal("classBodyDeclaration").addSemanticAction( (p, r) -> {

			p.setSemanticAttribute("node", r.get(0).getSemanticAttribute("node").get());

		});

		getNonTerminal("classMemberDeclaration").addSemanticAction( (p, r) -> {

			p.setSemanticAttribute("node", r.get(0).getSemanticAttribute("node").get());

		});


		getNonTerminal("fieldDeclaration").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				FieldDeclarationNode n = new FieldDeclarationNode();

				n.setType (ensureTypeNode(r.get(0).getAstNode().get()));

				n.setName(r.get(1).getAstNode(IdentifierNode.class).get());

				if (r.size() > 3){
					AstNode u = r.get(3).getAstNode().get();
					if ( u instanceof IdentifierNode) {
						u = new VariableReadNode(((IdentifierNode)u).getId());
					}
					n.setInicializer((ExpressionNode)u);

				}

				p.setAstNode(n);
			}
		});

		getNonTerminal("type").addSemanticAction( (p, r) -> {


			AstNode node = r.get(0).getAstNode().get();
			QualifiedNameNode name = null;
			if (node instanceof QualifiedNameNode){
				name = (QualifiedNameNode)node;
			} else if (node instanceof IdentifierNode){
				name = new QualifiedNameNode();
				name.append(((IdentifierNode)node).getId());
			}
			TypeNode type = new TypeNode(name);

			p.setAstNode(type);

			if (r.size() > 1){

				AstNode n = r.get(2).getAstNode().get();

				if (n instanceof ParametricTypesNode){
					type.setParametricTypes((ParametricTypesNode)n);
				} else {
					ParametricTypesNode generic = new ParametricTypesNode();
					generic.add(n);

					type.setParametricTypes(generic);
				}


			}


		});

		getNonTerminal("parametricTypes").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				AstNode node = r.get(0).getAstNode().get();
				if (node instanceof ParametricTypesNode){
					p.setAstNode(node);
				} else {
					ParametricTypesNode ptn = new ParametricTypesNode();
					ptn.add(node);
					p.setAstNode(ptn);
				}
			} else {
				AstNode n = r.get(0).getAstNode().get();

				if (n instanceof ParametricTypesNode){
					ParametricTypesNode node = (ParametricTypesNode)n;
					node.add(r.get(2).getAstNode().get());
					p.setAstNode(node);
				} else {
					ParametricTypesNode node = new ParametricTypesNode();
					node.add(r.get(0).getAstNode().get());
					node.add(r.get(2).getAstNode().get());
					p.setAstNode(node);
				}


			}


		});



		getNonTerminal("returnType").addSemanticAction( (p, r) -> {

			if (r.get(0).getAstNode().isPresent() && r.get(0).getAstNode().get() instanceof TypeNode){
				p.setAstNode(r.get(0).getAstNode().get());
			} else if (r.get(0).getSemanticAttribute("lexicalValue").get().equals("void")){
				p.setAstNode(new TypeNode(true));
			}

		});

		getNonTerminal("methodDeclaration").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setSemanticAttribute("node", r.get(0).getSemanticAttribute("node").get());
			} else {
				MethodNode n = (MethodNode) r.get(0).getSemanticAttribute("node").get();

				if (r.get(1).getAstNode().isPresent()){
					n.setBlock((BlockNode) r.get(1).getSemanticAttribute("node").get());
				}
				p.setAstNode(n);
			}

		});

		getNonTerminal("methodBody").addSemanticAction( (p, r) -> {
			if (r.get(0).getAstNode().isPresent()){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				p.setAstNode(new BlockNode());
			}
		});

		getNonTerminal("block").addSemanticAction( (p, r) -> {

			if (r .size() == 3){
				p.setSemanticAttribute("node",  r.get(1).getSemanticAttribute("node").get());
			} else if (r.size() == 1){
				if (r.get(0).getSemanticAttribute("node").isPresent()){
					p.setSemanticAttribute("node",  r.get(0).getSemanticAttribute("node").get());
				} else {
					p.setSemanticAttribute("node", new BlockNode());
				}

			}


		});


		getNonTerminal("maybeAnnotations").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("methodHeader").addSemanticAction( (p, r) -> {

			MethodNode n = new MethodNode();

			Optional<AnnotationListNode> annots = r.get(0).getAstNode(AnnotationListNode.class);

			if (annots.isPresent()){
				n.setAnnotationListNode(annots.get());
			}

			TypeNode type;
			if (r.get(1).getSemanticAttribute("node").isPresent()){
				type = r.get(1).getAstNode(TypeNode.class).get();
			} else {
				type = new TypeNode(true);
			}

			n.setReturnType(type);
			n.setName((String) r.get(2).getSemanticAttribute("lexicalValue").get());

			if (r.get(4).getSemanticAttribute("node").isPresent()){
				AstNode list = (AstNode)r.get(4).getSemanticAttribute("node").get();

				if (list instanceof VariableDeclarationNode){
					ParametersListNode ln = new ParametersListNode();
					ln.add(list);
					n.setParameters(ln);
				} else {
					n.setParameters((ParametersListNode)list);
				}
			}

			p.setSemanticAttribute("node", n);
		});

		getNonTerminal("blockStatements").addSemanticAction( (p, r) -> {

			AstNode other = r.get(0).getAstNode().get();
			BlockNode n;
			if (other instanceof BlockNode){
				n = (BlockNode)other;
				n.add(r.get(1).getAstNode().get());
			} else {
				n = new BlockNode();
				n.add(r.get(0).getAstNode().get());
				if (r.size() > 1){
					n.add(r.get(1).getAstNode().get());
				}
			}


			p.setAstNode(n);
		});

		getNonTerminal("blockStatement").addSemanticAction( (p, r) -> {

			p.setAstNode(r.get(0).getAstNode().get());
		});

		SemanticAction upstream = (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());		
		};

		getNonTerminal("statement").addSemanticAction( upstream);
		getNonTerminal("statementWithoutTrailingSubstatement").addSemanticAction( upstream);
		getNonTerminal("conditionalExpression").addSemanticAction( upstream);
		getNonTerminal("expressionStatement").addSemanticAction( upstream);
		getNonTerminal("statementExpression").addSemanticAction( upstream);
		getNonTerminal("constantExpression").addSemanticAction( upstream);

		getNonTerminal("postincrementExpression").addSemanticAction( (p, r) -> {
			PosExpression exp = new PosExpression(ArithmeticOperation.Addition);
			p.setAstNode(exp);		
		});

		getNonTerminal("postdecrementExpression").addSemanticAction( (p, r) -> {
			PosExpression exp = new PosExpression(ArithmeticOperation.Subtraction);
			p.setAstNode(exp);		
		});

		getNonTerminal("returnStatement").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				ReturnNode node = new ReturnNode();

				if (r.size() == 3){
					node.setValue(ensureExpression(r.get(1).getAstNode().get()));
				}

				p.setAstNode(node);
			}
		});

		getNonTerminal("breakStatement").addSemanticAction( (p, r) -> {

			BreakNode node = new BreakNode();

			// TODO add label

			p.setAstNode(node);

		});

		getNonTerminal("continueStatement").addSemanticAction( (p, r) -> {

			ContinueNode node = new ContinueNode();

			// TODO add label

			p.setAstNode(node);

		});

		getNonTerminal("tryStatement").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				TryStatement node = new TryStatement();

				if (r.get(1).getAstNode().isPresent()){
					node.setResource(r.get(1).getAstNode(ExpressionNode.class).get());

				}
				node.setInstructions(r.get(2).getAstNode(BlockNode.class).get());

				if (r.get(3).getAstNode().isPresent()){
					AstNode n = r.get(3).getAstNode().get();
					if (n instanceof CatchOptionNode){
						CatchOptionsNode c = new CatchOptionsNode();
						c.add(n);
						node.setCatchOptions(c);
					} else {
						node.setCatchOptions((CatchOptionsNode) n);
					}

				}

				if (r.get(4).getAstNode().isPresent()){
					// finally
					node.setFinally(r.get(4).getAstNode(BlockNode.class).get());
				}

				p.setAstNode(node);
			}
		});

		getNonTerminal("catches").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				AstNode n = r.get(0).getAstNode().get();
				if (n instanceof CatchOptionsNode){
					CatchOptionsNode node = (CatchOptionsNode)n;
					node.add(r.get(1).getAstNode().get());
					p.setAstNode(node);
				} else {
					CatchOptionsNode node = new CatchOptionsNode();
					node.add(r.get(0).getAstNode().get());
					node.add(r.get(1).getAstNode().get());
					p.setAstNode(node);
				}
			}
		});

		getNonTerminal("catchClause").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				CatchOptionNode node = new CatchOptionNode();

				node.setExceptions(r.get(2).getAstNode(VariableDeclarationNode.class).get());
				node.setInstructions(r.get(4).getAstNode(BlockNode.class).get());

				p.setAstNode(node);
			}
		});

		getNonTerminal("finally").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				p.setAstNode(r.get(1).getAstNode().get());
			}
		});

		getNonTerminal("ifThenStatement").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				DecisionNode node = new DecisionNode();

				node.setCondition(r.get(2).getAstNode(ExpressionNode.class).get());
				node.setTruePath(r.get(4).getAstNode(BlockNode.class).get());

				p.setAstNode(node);
			}

		});

		getNonTerminal("ifThenElseStatement").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				DecisionNode node = new DecisionNode();

				node.setCondition(r.get(2).getAstNode(ExpressionNode.class).get());
				node.setTruePath(r.get(4).getAstNode(BlockNode.class).get());
				node.setFalsePath(r.get(6).getAstNode(BlockNode.class).get());

				p.setAstNode(node);
			}

		});

		getNonTerminal("ifThenElseIfStatement").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				DecisionNode node = r.get(0).getAstNode(DecisionNode.class).get();
				DecisionNode alternative = (DecisionNode)node.getFalseBlock();

				alternative.setFalsePath(r.get(1).getAstNode().get());
				p.setAstNode(node);
			}

		});

		getNonTerminal("conditionsList").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				DecisionNode node = new DecisionNode();


				node.setCondition(r.get(2).getAstNode(ExpressionNode.class).get());
				node.setTruePath(r.get(4).getAstNode(BlockNode.class).get());

				DecisionNode node2 = new DecisionNode();

				node.setFalsePath(node2);

				node2.setCondition(r.get(8).getAstNode(ExpressionNode.class).get());
				node2.setTruePath(r.get(10).getAstNode(BlockNode.class).get());

				p.setAstNode(node);
			}

		});

		getNonTerminal("endAlternative").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else if (r.size() == 2){
				p.setAstNode(r.get(1).getAstNode(BlockNode.class).get());
			} else {
				DecisionNode node = new DecisionNode();

				node.setCondition(r.get(3).getAstNode(ExpressionNode.class).get());
				node.setTruePath(r.get(5).getAstNode(BlockNode.class).get());
				node.setFalsePath(r.get(6).getAstNode().get());

				p.setAstNode(node);
			}

		});

		getNonTerminal("whileStatement").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				WhileNode node = new WhileNode();

				node.setCondition(r.get(2).getAstNode(ExpressionNode.class).get());

				if (r.size() > 4 && r.get(4).getAstNode(BlockNode.class).isPresent()){
					node.setBlock(r.get(4).getAstNode(BlockNode.class).get());
				}

				p.setAstNode(node);
			}

		});

		getNonTerminal("forStatement").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				ForEachNode node = new ForEachNode();

				node.setIterableVariable(r.get(2).getAstNode(VariableDeclarationNode.class).get());
				node.setContainer(ensureExpression(r.get(4).getAstNode().get()));

				if (r.size() > 4 && r.get(6).getAstNode(BlockNode.class).isPresent()){
					node.setBlock(r.get(6).getAstNode(BlockNode.class).get());
				}

				p.setAstNode(node);
			}

		});

		getNonTerminal("iterationType").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				VariableDeclarationNode node = new VariableDeclarationNode();

				node.setType(ensureTypeNode(r.get(0).getAstNode().get()));
				node.setName(r.get(1).getAstNode(IdentifierNode.class).get());

				p.setAstNode(node);
			}

		});




		getNonTerminal("switchStatement").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				SwitchNode node = new SwitchNode();

				node.setCandidate(ensureExpression(r.get(2).getAstNode().get()));
				node.setOptions(r.get(4).getAstNode(SwitchOptions.class).get());

				p.setAstNode(node);
			}

		});

		getNonTerminal("switchBlock").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				p.setAstNode(r.get(1).getAstNode().get());
			}

		});

		getNonTerminal("switchLabels").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else {
				
				AstNode a = r.get(0).getAstNode().get();
				
				SwitchOptions node = new SwitchOptions();
				if (a instanceof SwitchOptions){
					node = (SwitchOptions)a;
					node.add(r.get(1).getAstNode().get());	
				} else {
					node.add(r.get(0).getAstNode().get());	
					node.add(r.get(1).getAstNode().get());
				}
				p.setAstNode(node);
			}

		});

		getNonTerminal("switchLabel").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());		
			} else if (r.size() == 2){
				SwitchOption node = new SwitchOption(true);
				node.setActions(r.get(1).getAstNode(BlockNode.class).get());
				p.setAstNode(node);
			} else {
				SwitchOption node = new SwitchOption();

				node.setValue(ensureExpression(r.get(2).getAstNode().get()));
				node.setActions(r.get(4).getAstNode(BlockNode.class).get());

				p.setAstNode(node);
			}

		});




		getNonTerminal("localVariableDeclarationStatement").addSemanticAction( (p, r) -> {

			if (r.get(0).getSemanticAttribute("node").isPresent() && r.get(0).getSemanticAttribute("node").get() instanceof VariableDeclarationNode){
				p.setSemanticAttribute("node", r.get(0).getSemanticAttribute("node").get() );
			} else {
				VariableDeclarationNode n = new VariableDeclarationNode();

				if (r.get(0).getSemanticAttribute("lexicalValue").isPresent()){
					if (r.get(0).getSemanticAttribute("lexicalValue").get().equals("void")){
						TypeNode t = new TypeNode(true);
						n.setType(t);
					} else {
						TypeNode t = new TypeNode(r.get(0).getAstNode(QualifiedNameNode.class).get());
						n.setType(t);
					}

				} else {
					n.setType(ensureTypeNode(r.get(0).getAstNode().get()));
				}

				n.setName(r.get(1).getAstNode(IdentifierNode.class).get());

				if (r.size() > 3){
					n.setInitializer(r.get(3).getAstNode(ExpressionNode.class).get());
				}

				p.setSemanticAttribute("node", n);
			}


		});

		getNonTerminal("formalParameterList").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else  if (r.size() == 3){

				ParametersListNode list;
				if (r.get(0).getAstNode().get() instanceof ParametersListNode){
					list = r.get(0).getAstNode(ParametersListNode.class).get();
					list.add(r.get(2).getAstNode().get());
				} else {
					list = new ParametersListNode();
					list.add(r.get(0).getAstNode().get());
					list.add(r.get(2).getAstNode().get());
				}
				p.setAstNode(list);
			}

		});

		getNonTerminal("variableName").addSemanticAction( (p, r) -> {

			if (r.get(0).getAstNode().isPresent()){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				p.setAstNode(new IdentifierNode((String)r.get(0).getSemanticAttribute("lexicalValue").get()));
			}

		});

		getNonTerminal("formalParameter").addSemanticAction( (p, r) -> {
			if (r.get(0).getSemanticAttribute("node").isPresent() && r.get(0).getSemanticAttribute("node").get() instanceof VariableDeclarationNode){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				VariableDeclarationNode n = new VariableDeclarationNode();

				if (r.get(0).getSemanticAttribute("lexicalValue").isPresent()){
					if (r.get(0).getSemanticAttribute("lexicalValue").get().equals("void")){
						TypeNode t = new TypeNode(true);
						n.setType(t);
					} else {
						TypeNode t = new TypeNode(r.get(0).getAstNode(QualifiedNameNode.class).get());
						n.setType(t);
					}

				} else {
					n.setType(ensureTypeNode(r.get(0).getAstNode().get()));
				}


				if (r.get(1).getSemanticAttribute("lexicalValue").isPresent()){
					IdentifierNode v = new IdentifierNode((String)r.get(1).getSemanticAttribute("lexicalValue").get());
					n.setName(v);
				} else {
					n.setName((IdentifierNode) r.get(1).getSemanticAttribute("node").get());
				}

				p.setSemanticAttribute("node", n);
			}
		});

		getNonTerminal("expression").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());		
		});

		getNonTerminal("conditionalExpression").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());		
		});

		getNonTerminal("ternaryExpression").addSemanticAction( (p, r) -> {

			TernaryConditionalExpressionNode node = new TernaryConditionalExpressionNode();

			node.Condition(r.get(0).getAstNode(ExpressionNode.class).get());
			node.TruePath(r.get(2).getAstNode(ExpressionNode.class).get());
			node.FalsePath(r.get(3).getAstNode(ExpressionNode.class).get());

			p.setAstNode(node);

		});


		SemanticAction booleanArithmetics = (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {

				BooleanOperatorNode exp = new BooleanOperatorNode(resolveBooleanOperation(r.get(1)));
				exp.add(r.get(0).getAstNode().get());
				exp.add(r.get(2).getAstNode().get());
				p.setAstNode(exp);
			}			
		};


		getNonTerminal("conditionalOrExpression").addSemanticAction(booleanArithmetics);
		getNonTerminal("conditionalAndExpression").addSemanticAction(booleanArithmetics);
		getNonTerminal("inclusiveOrExpression").addSemanticAction(booleanArithmetics);
		getNonTerminal("exclusiveOrExpression").addSemanticAction(booleanArithmetics);
		getNonTerminal("andExpression").addSemanticAction(booleanArithmetics);

		getNonTerminal("equalityExpression").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				ComparisonNode exp = new ComparisonNode(resolveComparisonOperation(r.get(1)));
				exp.add(ensureExpression(r.get(0).getAstNode().get()));
				exp.add(ensureExpression(r.get(2).getAstNode().get()));
				p.setAstNode(exp);
			}			
		});

		getNonTerminal("relationalExpression").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				ComparisonNode exp = new ComparisonNode(resolveComparisonOperation(r.get(1)));
				exp.add(ensureExpression(r.get(0).getAstNode().get()));
				exp.add(ensureExpression(r.get(1).getAstNode().get()));
				p.setAstNode(exp);
			}			
		});


		SemanticAction arithmetics = (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {

				ArithmeticNode exp = new ArithmeticNode(resolveOperation(r.get(1)));
				exp.add(ensureExpression(r.get(0).getAstNode().get()));
				exp.add(ensureExpression(r.get(2).getAstNode().get()));
				p.setAstNode(exp);
			}			
		};
		getNonTerminal("shiftExpression").addSemanticAction(arithmetics);
		getNonTerminal("multiplicativeExpression").addSemanticAction(arithmetics);

		getNonTerminal("additiveExpression").addSemanticAction((p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {

				ArithmeticNode exp = new ArithmeticNode(resolveOperation(r.get(1)));
				exp.add(ensureExpression(r.get(0).getAstNode().get()));
				exp.add(ensureExpression(r.get(2).getAstNode().get()));
				p.setAstNode(exp);
			}			
		});

		getNonTerminal("rangeExpression").addSemanticAction((p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {

				RangeNode exp = new RangeNode();
				exp.add(ensureExpression(r.get(0).getAstNode().get()));
				exp.add(ensureExpression(r.get(2).getAstNode().get()));
				p.setAstNode(exp);
			}			
		});

		getNonTerminal("unaryExpression").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				PosExpression  exp = new PosExpression(resolveOperation(r.get(0)));
				exp.add(r.get(1).getAstNode().get());
				p.setAstNode(exp);
			}			
		});

		getNonTerminal("unaryExpressionNotPlusMinus").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				PosUnaryExpression exp = new PosUnaryExpression(resolveBooleanOperation(r.get(0)));
				exp.add(r.get(1).getAstNode(ExpressionNode.class).get());
				p.setAstNode(exp);
			}			
		});

		getNonTerminal("castExpression").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("postfixExpression").addSemanticAction( (p, r) -> {

			if (r.get(0).getAstNode().isPresent()){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				p.setAstNode(new IdentifierNode(r.get(0).getLexicalValue()));
			}

		});

		getNonTerminal("assignment").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				AssignmentNode node= new AssignmentNode(resolveAssignmentOperation(r.get(1)));
				node.setLeft (r.get(0).getAstNode().get());
				node.setRight (ensureExpression(r.get(2).getAstNode().get()));

				p.setAstNode(node);
			}

		});


		getNonTerminal("assignmentOperator").addSemanticAction( (p, r) -> {
			p.setSemanticAttribute("lexicalValue", r.get(0).getSemanticAttribute("lexicalValue").get());
		});

		getNonTerminal("leftHandSide").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("variableWrite").addSemanticAction( (p, r) -> {
			if (r.size() == 1 && r.get(0).getAstNode().isPresent() && r.get(0).getAstNode().get() instanceof VariableReadNode){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				VariableWriteNode v = new VariableWriteNode((String)r.get(0).getSemanticAttribute("lexicalValue").get());
				p.setAstNode(v);
			}
		});

		getNonTerminal("assignmentOperator").addSemanticAction( (p, r) -> {
			p.setSemanticAttribute("lexicalValue",r.get(0).getSemanticAttribute("lexicalValue").get());

		});


		getNonTerminal("classInstanceCreationExpression").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				ClassInstanceCreation node = new ClassInstanceCreation();

				AstNode t = r.get(1).getAstNode().get();

				node.setType (ensureTypeNode(t));


				if (r.size() > 4){
					AstNode n = r.get(3).getAstNode().get();
					if (n instanceof ArgumentListNode){
						node.setArguments ((ArgumentListNode)n);
					} else if (n instanceof ExpressionNode){
						ArgumentListNode args = new ArgumentListNode();
						args.add(n);
						node.setArguments (args);
					}

				}

				p.setAstNode(node);
			}

		});

		getNonTerminal("argumentList").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else if (r.get(0).getAstNode().get() instanceof ArgumentListNode ){
				ArgumentListNode node = r.get(0).getAstNode(ArgumentListNode.class).get();
				node.add(r.get(2).getAstNode().get());
				p.setAstNode(node);
			} else {
				ArgumentListNode node = new ArgumentListNode();
				node.add(r.get(0).getAstNode().get());
				node.add(r.get(2).getAstNode().get());
				p.setAstNode(node);
			}


		});

		getNonTerminal("methodInvocation").addSemanticAction( (p, r) -> {
			if (r.size() == 1 && (r.get(0).getAstNode().get() instanceof MethodInvocationNode)){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				MethodInvocationNode node = new MethodInvocationNode();
				if (r.size() == 1){
					node.setCall(r.get(0).getAstNode(MethodCallNode.class).get());
					// implicit self access
				} else if (r.size() == 4){

					MethodCallNode call = new MethodCallNode();
					QualifiedNameNode name = r.get(0).getAstNode(QualifiedNameNode.class).get();

					if (name.getName().contains(".")){

						int pos = name.getName().lastIndexOf('.');
						call.setName(name.getName().substring(pos+1));

						node.setAccess(new QualifiedNameNode(name.getName().substring(0, pos)));
					} else {
						call.setName(name.getName());
					}


					if (r.get(2).getAstNode().isPresent()){
						call.setArgumentList(r.get(2).getAstNode(ArgumentListNode.class).get());
					} else {
						call.setArgumentList(new ArgumentListNode());
					}

					node.setCall(call);

				} else if (r.size() == 6){
					MethodCallNode call = new MethodCallNode();
				
					node.setAccess(r.get(0).getAstNode().get());
					
					call.setName(r.get(2).getLexicalValue());
				

					if (r.get(5).getAstNode().isPresent()){
						call.setArgumentList(r.get(5).getAstNode(ArgumentListNode.class).get());
					} else {
						call.setArgumentList(new ArgumentListNode());
					}

					node.setCall(call);
				}
				p.setAstNode(node);
			}

		});

		getNonTerminal("maybeArgumentList").addSemanticAction( (p, r) -> {
			if (r.size() == 1 && (r.get(0).getAstNode().get() instanceof ArgumentListNode)){
				p.setAstNode(r.get(0).getAstNode().get());
			} 
		});
		//		getNonTerminal("methodCall").addSemanticAction( (p, r) -> {
		//			if (r.size() == 1){
		//				p.setAstNode(r.get(0).getAstNode().get());
		//			} else {
		//				MethodCallNode node = new MethodCallNode();
		//				node.setName((String)r.get(0).getSemanticAttribute("lexicalValue").get());
		//
		//				if (r.size() > 3){
		//					AstNode n = r.get(2).getAstNode().get();
		//					if (n instanceof ArgumentListNode){
		//						node.setArgumentList((ArgumentListNode) n);
		//					} else if (n instanceof ExpressionNode) {
		//						ArgumentListNode args = new ArgumentListNode();
		//						args.add(n);
		//						node.setArgumentList(args);
		//					}
		//
		//				}
		//				p.setAstNode(node);
		//			}
		//
		//		});


		getNonTerminal("fieldAccess").addSemanticAction( (p, r) -> {
			if (r.size()==1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				FieldAccessNode node = new FieldAccessNode();
				node.setName((String)r.get(2).getSemanticAttribute("lexicalValue").get());
				p.setAstNode(node);
			}
		});

		getNonTerminal("arrayAccess").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				IndexedAccessNode  node = new IndexedAccessNode();
				// TODO receives Qualified name. Shouble field access
				node.setAccess(ensureExpression(r.get(0).getAstNode().get()));
				node.setIndexExpression(r.get(2).getAstNode(ExpressionNode.class).get());

				p.setAstNode(node);
			}

		});

		getNonTerminal("primary").addSemanticAction( (p, r) -> {
			if (r.size() == 3){
				p.setAstNode(r.get(0).getAstNode().get());
			} else if (r.get(0).getAstNode().isPresent()){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				// 'this'
				if ("this".equals(r.get(0).getSemanticAttribute("lexicalValue").get())){
					p.setAstNode(new VariableReadNode("this"));
				} else {
					p.setAstNode(r.get(0).getAstNode().get());
				}
			}

		});

		getNonTerminal("literal").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("numberLiteral").addSemanticAction( (p, r) -> {

			String number = (String)r.get(0).getSemanticAttribute("lexicalValue").get();

			NumericValue v = new NumericValue();
			if (!Character.isDigit(number.charAt(number.length()-1))){
				final BigDecimal n = new BigDecimal(number.substring(0, number.length() - 1));
				v.setValue( n, determineType(number)); 
			} else {
				final BigDecimal n = new BigDecimal(number);
				v.setValue( n, determineType(number)); 
			}

			p.setAstNode(v);

		});

		getNonTerminal("booleanLiteral").addSemanticAction( (p, r) -> {

			BooleanValue n = new BooleanValue();
			
			n.setValue("true".equals(r.get(0).getLexicalValue()));
			
			p.setAstNode(n);

		});

		getNonTerminal("stringLiteral").addSemanticAction( (p, r) -> {

			StringValue n = new StringValue();
			
			n.setValue(r.get(0).getLexicalValue());
			
			p.setAstNode(n);

		});

		getNonTerminal("nullLiteral").addSemanticAction( (p, r) -> {

			NullValue n = new NullValue();

			p.setAstNode(n);

		});


		getNonTerminal("superDeclaration").addSemanticAction( (p, r) -> {
			if (r.get(1).getSemanticAttribute("lexicalValue").isPresent()){
				QualifiedNameNode n = new QualifiedNameNode();

				n.append((String)r.get(1).getSemanticAttribute("lexicalValue").get());

				p.setAstNode(n);
			} else {
				p.setAstNode(r.get(1).getAstNode().get());		
			}

		});

		//		getNonTerminal("interfaceDeclaration").addSemanticAction( (p, r) -> {
		//			if (r.size() == 3){
		//				ClassType n = new ClassType();
		//				n.setName((String)r.get(1).getSemanticAttribute("lexicalValue").get());
		//				
		//				n.setBody((ClassBody)r.get(2).getSemanticAttribute("node").get());
		//				p.setSemanticAttribute("node", n);
		//			} else {
		//				p.setSemanticAttribute("node",r.get(0).getSemanticAttribute("node").get());
		//			}
		//			
		//		});

		//		getNonTerminal("interfaceBody").addSemanticAction( (p, r) -> {
		//			ClassBody n = new ClassBody();
		//			p.setSemanticAttribute("node", n);
		//			
		//		});
		//		

		// TODO make alternative auto-merge with other alternatives . implemente GLR splitstak parsing

	}


	/**
	 * @param astNode
	 * @return
	 */
	private ExpressionNode ensureExpression(AstNode node) {
		if (node instanceof  IdentifierNode){
			return new VariableReadNode(((IdentifierNode)node).getId());
		} else if (node instanceof  QualifiedNameNode){
			return new VariableReadNode(((QualifiedNameNode)node).getName());
		} else {
			return (ExpressionNode)node;
		}
	}



	/**
	 * @param object
	 * @return
	 */
	private QualifiedNameNode ensureQualifiedName(AstNode node) {
		if (node instanceof QualifiedNameNode){
			return (QualifiedNameNode)node;
		} else if (node instanceof IdentifierNode){
			QualifiedNameNode q = new QualifiedNameNode();
			q.append(((IdentifierNode)node).getId());
			return q;
		} else {
			throw new RuntimeException();
		}
	}


	private TypeNode ensureTypeNode(AstNode t) {
		if (t instanceof TypeNode){
			return (TypeNode) t;
		} else {
			return new TypeNode((QualifiedNameNode)t);
		}
	}


	/**
	 * @param semanticAttribute
	 * @return
	 */
	private AssignmentNode.Operation resolveAssignmentOperation(Symbol symbol) {

		String op = (String)((Symbol)symbol.getParserTreeNode().getChildren().get(0)).getSemanticAttribute("lexicalValue").get();


		switch (op) {
		case "=":
			return AssignmentNode.Operation.SimpleAssign;
		case "*=":
			return AssignmentNode.Operation.MultiplyAndAssign;
		case "/=":
			return AssignmentNode.Operation.DivideAndAssign;
		case "%=":
			return AssignmentNode.Operation.RemainderAndAssign;
		case "+=":
			return AssignmentNode.Operation.AddAndAssign;
		case "-=":
			return AssignmentNode.Operation.SubtractAndAssign;
		case "<<=":
			return AssignmentNode.Operation.LeftShiftAndAssign;
		case ">>=":
			return AssignmentNode.Operation.RightShiftAndAssign;
		case ">>>=":
			return AssignmentNode.Operation.PositiveRightShiftAndAssign;
		case "&=":
			return AssignmentNode.Operation.BitAndAndAssign;
		case "^=":
			return AssignmentNode.Operation.BitXorAndAssign;
		case "|=":
			return AssignmentNode.Operation.BitOrAndAssign;
		default:
			throw new RuntimeException(op + "is not a recognized operator");
		}
	}


	/**
	 * @param string
	 * @return
	 */
	private BooleanOperatorNode.BooleanOperation resolveBooleanOperation(Symbol symbol) {

		String op;
		if (symbol.getSemanticAttribute("lexicalValue").isPresent()){
			op = (String)symbol.getSemanticAttribute("lexicalValue").get();
		} else {
			op = (String)((Symbol)symbol.getParserTreeNode().getChildren().get(0)).getSemanticAttribute("lexicalValue").get();
		}

		switch (op) {
		case "&":
			return BooleanOperatorNode.BooleanOperation.BitAnd;
		case "&&":
			return BooleanOperatorNode.BooleanOperation.LogicShortAnd;
		case "|":
			return BooleanOperatorNode.BooleanOperation.BitOr;
		case "||":
			return BooleanOperatorNode.BooleanOperation.LogicShortOr;
		case "^":
			return BooleanOperatorNode.BooleanOperation.BitXor;
		case "~":
			return BooleanOperatorNode.BooleanOperation.BitNegate;
		case "!":
			return BooleanOperatorNode.BooleanOperation.LogicNegate;
		default:
			throw new RuntimeException(op + "is not a recognized operator");
		}
	}


	/**
	 * @param string
	 * @return
	 */
	private ComparisonNode.Operation resolveComparisonOperation(Symbol symbol) {


		String op = (String)((Symbol)symbol.getParserTreeNode().getChildren().get(0)).getSemanticAttribute("lexicalValue").get();

		switch (op) {
		case ">":
			return ComparisonNode.Operation.GreaterThan;
		case "<":
			return ComparisonNode.Operation.LessThan;
		case ">=":
			return ComparisonNode.Operation.GreaterOrEqualTo;
		case "<=":
			return ComparisonNode.Operation.LessOrEqualTo;
		case "==":
			return ComparisonNode.Operation.EqualTo;
		case "!=":
			return ComparisonNode.Operation.Different;
		case "===":
			return ComparisonNode.Operation.ReferenceEquals;
		default:
			throw new RuntimeException(op + "is not a recognized operator");
		}
	}


	public static ArithmeticOperation resolveOperation(Symbol symbol){
		String op;
		if (symbol.getSemanticAttribute("lexicalValue").isPresent()){
			op = (String)symbol.getSemanticAttribute("lexicalValue").get();
		} else {
			op = (String)((Symbol)symbol.getParserTreeNode().getChildren().get(0)).getSemanticAttribute("lexicalValue").get();
		}

		switch (op) {
		case "+":
			return ArithmeticOperation.Addition;
		case "++":
			return ArithmeticOperation.Increment;
		case "--":
			return ArithmeticOperation.Decrement;
		case "-":
			return ArithmeticOperation.Subtraction;
		case "*":
			return ArithmeticOperation.Multiplication;
		case "/":
			return ArithmeticOperation.Division;
		case "%":
			return ArithmeticOperation.Remainder;
		case "//":
			return ArithmeticOperation.FractionDivision;
		case ">>":
			return ArithmeticOperation.RightShift;
		case "<<":
			return ArithmeticOperation.LeftShift;
		case ">>>":
			return ArithmeticOperation.SignedRightShift;
		default:
			throw new RuntimeException(op + "is not a recognized operator");
		}

	}




}
