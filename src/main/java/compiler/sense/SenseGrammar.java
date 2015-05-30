/**
 * 
 */
package compiler.sense;

import java.util.Optional;
import java.util.Set;

import compiler.TokenSymbol;
import compiler.SymbolBasedToken;
import compiler.lexer.ScanPosition;
import compiler.lexer.Token;
import compiler.parser.BottomUpParser;
import compiler.parser.IdentifierNode;
import compiler.parser.LALRAutomatonFactory;
import compiler.parser.Parser;
import compiler.parser.SemanticAction;
import compiler.parser.Symbol;
import compiler.syntax.AstNode;


/**
 * 
 */
public class SenseGrammar extends AbstractSenseGrammar{


	public Parser parser() {
		return new BottomUpParser(this, new LALRAutomatonFactory());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIgnore(char c) {
		return c == '\t' || c == '\r' ||  c== ' ' || c == '\n' ;
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
		} else if (text.matches("^\\d+$")){
			return  Optional.of(new SymbolBasedToken(pos, text, TokenSymbol.LiteralWholeNumber));
		} else if (text.matches("[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?")){
			return  Optional.of(new SymbolBasedToken(pos,text, TokenSymbol.LiteralFloatPointNumber));
		} 
//		else if (text.equals("do")){
//			return  Optional.of(new SymbolBasedToken(pos,"do", TokenSymbol.KeyWord));
//		}
		
		return super.terminalMatch(pos, text);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Token> stringLiteralMath(ScanPosition pos,String text) {
		return Optional.of(new SymbolBasedToken(pos, text, TokenSymbol.LiteralString));
	}

	protected void posInit() {
		installSemanticActions();
	}

	protected void installSemanticActions() {

		getNonTerminal("qualifiedName").addSemanticAction((p,r)->{

			QualifiedName name = new QualifiedName();

			for (int i = 0; i< r.size(); i+=2){
				if (r.get(i).getAstNode().isPresent()){
					AstNode node = r.get(i).getAstNode().get();
					if (node instanceof QualifiedName){
						name =  (QualifiedName)node;
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
				QualifiedName packageName = (QualifiedName)r.get(0).getSemanticAttribute("node").get();

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
				if (r.get(1).getSemanticAttribute("node").isPresent()){
					ImportsNode imports = (ImportsNode) r.get(1).getSemanticAttribute("node").get();
					types.add(imports);
				}
				p.setSemanticAttribute("node", types);

			} else {
				types = new UnitTypes();
				p.setSemanticAttribute("node", types);
			}



		});

		getNonTerminal("importDeclarations").addSemanticAction( (p, r) -> {
			ImportsNode q = (ImportsNode) r.get(0).getSemanticAttribute("node").get();
			if (r.size() > 1 && r.get(1).getSemanticAttribute("node").isPresent()){
				q.add((AstNode) r.get(1).getSemanticAttribute("node").get());
			}

			p.setSemanticAttribute("node", q);
		});

		getNonTerminal("importDeclaration").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				ImportsNode q = new ImportsNode();
				q.add((AstNode) r.get(0).getSemanticAttribute("node").get());	
				p.setSemanticAttribute("node", q);
			} else {
				ImportNode q = new ImportNode();
				q.setName(r.get(1).getAstNode(QualifiedName.class).get());
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

		getNonTerminal("classDeclaration").addSemanticAction( (p, r) -> {
			if (r.size() == 4){
				ClassType n = new ClassType();
				n.setName((String)r.get(1).getSemanticAttribute("lexicalValue").get());

				Optional<Object> ext = r.get(2).getSemanticAttribute("node");

				if (ext.isPresent()){
					n.setUpperType((QualifiedName)ext.get());
				}

				n.setBody((ClassBody)r.get(3).getSemanticAttribute("node").get());
				p.setSemanticAttribute("node", n);
			} else {
				p.setSemanticAttribute("node",r.get(0).getSemanticAttribute("node").get());
			}

		});


		getNonTerminal("classBody").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(1).getAstNode().get());
		});

		getNonTerminal("classBodyDeclarations").addSemanticAction( (p, r) -> {

			ClassBody body = new ClassBody();
			AstNode node = r.get(0).getAstNode().get();
			if ( node instanceof ClassBody){
				body = (ClassBody)node;

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
				n.setType(r.get(0).getAstNode(TypeNode.class).get());
				n.setVariableName(r.get(1).getAstNode(VariableNameNode.class).get());

				p.setAstNode(n);
			}
		});

		getNonTerminal("type").addSemanticAction( (p, r) -> {

			AstNode node = r.get(0).getAstNode().get();
			QualifiedName name = null;
			if (node instanceof QualifiedName){
				name = (QualifiedName)node;
			} else if (node instanceof IdentifierNode){
				name = new QualifiedName();
				name.append(((IdentifierNode)node).getId());
			}
			TypeNode type = new TypeNode(name);

			p.setSemanticAttribute("node", type);


		});

		getNonTerminal("returnType").addSemanticAction( (p, r) -> {

			AstNode node = r.get(0).getAstNode().get();

			if (node instanceof TypeNode){
				p.setAstNode(node);
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
			p.setAstNode(r.get(0).getAstNode().get());
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

		getNonTerminal("methodHeader").addSemanticAction( (p, r) -> {

			MethodNode n = new MethodNode();
			TypeNode type;
			if (r.get(0).getSemanticAttribute("node").isPresent()){
				type = r.get(0).getAstNode(TypeNode.class).get();
			} else {
				type = new TypeNode(true);
			}

			n.setReturnType(type);
			n.setName((String) r.get(1).getSemanticAttribute("lexicalValue").get());

			if (r.get(3).getSemanticAttribute("node").isPresent()){
				AstNode list = (AstNode)r.get(3).getSemanticAttribute("node").get();

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


			BlockNode n = r.get(0).getAstNode(BlockNode.class).get();
			if (r.get(1).getAstNode().isPresent()){
				n.add(r.get(1).getAstNode().get());
			}		

			p.setSemanticAttribute("node", n);
		});

		getNonTerminal("blockStatement").addSemanticAction( (p, r) -> {
			BlockNode n = new BlockNode();
			for (Symbol s : r){
				n.add(s.getAstNode().get());
			}
			p.setAstNode(n);
		});

		getNonTerminal("statement").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());		
		});

		getNonTerminal("statementWithoutTrailingSubstatement").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());		
		});

		getNonTerminal("expressionStatement").addSemanticAction( (p, r) -> {		
			p.setAstNode(r.get(0).getAstNode().get());		
		});


		getNonTerminal("statementExpression").addSemanticAction( (p, r) -> {	
			p.setAstNode(r.get(0).getAstNode().get());		
		});

		getNonTerminal("postincrementExpression").addSemanticAction( (p, r) -> {
			PosExpression exp = new PosExpression(ArithmeticNode.Operation.Addition);
			p.setAstNode(exp);		
		});

		getNonTerminal("postdecrementExpression").addSemanticAction( (p, r) -> {
			PosExpression exp = new PosExpression(ArithmeticNode.Operation.Subtraction);
			p.setAstNode(exp);		
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
						TypeNode t = new TypeNode(r.get(0).getAstNode(QualifiedName.class).get());
						n.setType(t);
					}

				} else {
					n.setType((TypeNode) r.get(0).getSemanticAttribute("node").get());
				}

				n.setName((VariableNameNode) r.get(1).getSemanticAttribute("node").get());


				p.setSemanticAttribute("node", n);
			}


		});


		getNonTerminal("localVariableDeclaration").addSemanticAction( (p, r) -> {

			if (r.get(0).getSemanticAttribute("node").isPresent() && r.get(0).getSemanticAttribute("node").get() instanceof VariableDeclarationNode){
				p.setSemanticAttribute("node", r.get(0).getSemanticAttribute("node").get() );
			} else {
				VariableDeclarationNode n = new VariableDeclarationNode();

				if (r.get(0).getSemanticAttribute("lexicalValue").isPresent()){
					if (r.get(0).getSemanticAttribute("lexicalValue").get().equals("void")){
						TypeNode t = new TypeNode(true);
						n.setType(t);
					} else {
						TypeNode t = new TypeNode(r.get(0).getAstNode(QualifiedName.class).get());
						n.setType(t);
					}

				} else {
					n.setType((TypeNode) r.get(0).getSemanticAttribute("node").get());
				}


				if (r.get(1).getSemanticAttribute("lexicalValue").isPresent()){
					VariableNameNode v = new VariableNameNode();
					v.setName((String)r.get(1).getSemanticAttribute("lexicalValue").get());
					n.setName(v);


				} else if (r.get(1).getSemanticAttribute("node").isPresent()){
					n.setName((VariableNameNode) r.get(1).getSemanticAttribute("node").get());
				} else {
					n.setName(null); // TODO add actions to variablename production
				}

				p.setSemanticAttribute("node", n);
			}

		});

		getNonTerminal("formalParameterList").addSemanticAction( (p, r) -> {

			if (r.size() == 3){
				VariableNameNode n = new VariableNameNode();
				n.setName((String)r.get(0).getSemanticAttribute("lexicalValue").get());
				n.setInitialValue(r.get(2).getAstNode(ExpressionNode.class).get());
				p.setSemanticAttribute("node", n);
			}

		});

		getNonTerminal("formalParameter").addSemanticAction( (p, r) -> {
			if (r.get(0).getSemanticAttribute("node").isPresent() && r.get(0).getSemanticAttribute("node").get() instanceof VariableDeclarationNode){
				p.setSemanticAttribute("node", r.get(0).getSemanticAttribute("node").get() );
			} else {
				VariableDeclarationNode n = new VariableDeclarationNode();

				if (r.get(0).getSemanticAttribute("lexicalValue").isPresent()){
					if (r.get(0).getSemanticAttribute("lexicalValue").get().equals("void")){
						TypeNode t = new TypeNode(true);
						n.setType(t);
					} else {
						TypeNode t = new TypeNode(r.get(0).getAstNode(QualifiedName.class).get());
						n.setType(t);
					}

				} else {
					n.setType((TypeNode) r.get(0).getSemanticAttribute("node").get());
				}


				if (r.get(1).getSemanticAttribute("lexicalValue").isPresent()){
					VariableNameNode v = new VariableNameNode();
					v.setName((String)r.get(1).getSemanticAttribute("lexicalValue").get());
					n.setName(v);


				} else {
					n.setName((VariableNameNode) r.get(1).getSemanticAttribute("node").get());
				}

				p.setSemanticAttribute("node", n);
			}
		});

		getNonTerminal("variableDeclarator").addSemanticAction( (p, r) -> {

			if (r.size() == 3){
				VariableNameNode n = new VariableNameNode();
				n.setName((String)r.get(0).getSemanticAttribute("lexicalValue").get());
				n.setInitialValue(r.get(2).getAstNode(ExpressionNode.class).get());
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

			ConditionalExpressionNode node = new ConditionalExpressionNode();

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
				exp.add(r.get(0).getAstNode().get());
				exp.add(r.get(1).getAstNode().get());
				p.setAstNode(exp);
			}			
		});

		getNonTerminal("relationalExpression").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				ComparisonNode exp = new ComparisonNode(resolveComparisonOperation(r.get(1)));
				exp.add(r.get(0).getAstNode().get());
				exp.add(r.get(1).getAstNode().get());
				p.setAstNode(exp);
			}			
		});


		SemanticAction arithmetics = (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {

				ArithmeticNode exp = new ArithmeticNode(resolveOperation(r.get(1)));
				exp.add(r.get(0).getAstNode().get());
				exp.add(r.get(2).getAstNode().get());
				p.setAstNode(exp);
			}			
		};
		getNonTerminal("shiftExpression").addSemanticAction(arithmetics);
		getNonTerminal("additiveExpression").addSemanticAction(arithmetics);
		getNonTerminal("multiplicativeExpression").addSemanticAction(arithmetics);

		getNonTerminal("unaryExpression").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				PosArithmeticUnaryExpression  exp = new PosArithmeticUnaryExpression(resolveOperation(r.get(0)));
				exp.add(r.get(1).getAstNode().get());
				p.setAstNode(exp);
			}			
		});

		getNonTerminal("unaryExpressionNotPlusMinus").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				PosUnaryExpression exp = new PosUnaryExpression(resolveBooleanOperation(r.get(0)));
				exp.add(r.get(1).getAstNode().get());
				p.setAstNode(exp);
			}			
		});

		getNonTerminal("castExpression").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("postfixExpression").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("assignment").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				AssignmentNode node= new AssignmentNode(resolveAssignmentOperation(r.get(1)));
				node.setLeft (r.get(0).getAstNode().get());
				node.setRight (r.get(2).getAstNode().get());

				p.setAstNode(node);
			}

		});


		getNonTerminal("assignmentOperator").addSemanticAction( (p, r) -> {
			p.setSemanticAttribute("lexicalValue", r.get(0).getSemanticAttribute("lexicalValue").get());
		});

		getNonTerminal("leftHandSide").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("assignmentOperator").addSemanticAction( (p, r) -> {
			p.setSemanticAttribute("lexicalValue",r.get(0).getSemanticAttribute("lexicalValue").get());

		});


		getNonTerminal("classInstanceCreationExpression").addSemanticAction( (p, r) -> {

			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				ClassInstanceCreation node = new ClassInstanceCreation();
				node.setType (r.get(1).getAstNode(TypeNode.class).get());

				if (r.size() > 3){
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
				} else if (r.size() == 3){
					if (r.get(0).getAstNode().isPresent()){
						node.setAccess(r.get(0).getAstNode().get());
					} else {
						// explicit self access
					}
					
					node.setCall(r.get(2).getAstNode(MethodCallNode.class).get());
				}
				p.setAstNode(node);
			}
			
		});
		
		getNonTerminal("methodCall").addSemanticAction( (p, r) -> {
			if (r.size() == 1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				MethodCallNode node = new MethodCallNode();
				node.setName((String)r.get(0).getSemanticAttribute("lexicalValue").get());
				
				if (r.size() > 3){
					AstNode n = r.get(2).getAstNode().get();
					if (n instanceof ArgumentListNode){
						node.setArgumentList((ArgumentListNode) n);
					} else if (n instanceof ExpressionNode) {
						ArgumentListNode args = new ArgumentListNode();
						args.add(n);
						node.setArgumentList(args);
					}
					
				}
				p.setAstNode(node);
			}
		
		});


		getNonTerminal("fieldAccess").addSemanticAction( (p, r) -> {
			if (r.size()==1){
				p.setAstNode(r.get(0).getAstNode().get());
			} else {
				FieldAcessNode node = new FieldAcessNode();
				node.setName((String)r.get(2).getSemanticAttribute("lexicalValue").get());
				p.setAstNode(node);
			}
		});

		getNonTerminal("arrayAccess").addSemanticAction( (p, r) -> {
			IndexedAccessNode  node = new IndexedAccessNode();
			p.setAstNode(node);
		});

		getNonTerminal("primary").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("literal").addSemanticAction( (p, r) -> {
			p.setAstNode(r.get(0).getAstNode().get());
		});

		getNonTerminal("numberLiteral").addSemanticAction( (p, r) -> {
			
			String number = (String)((Symbol)r.get(0).getParserTreeNode().getChildren().get(0)).getSemanticAttribute("lexicalValue").get();

			Number n = 0;
			NumericValue v = new NumericValue();
			v.setType(new TypeNode(new QualifiedName("Whole")));
			v.setValue(n);
			p.setAstNode(v);

		});

		getNonTerminal("booleanLiteral").addSemanticAction( (p, r) -> {

			BooleanValue n = new BooleanValue();
			n.setType(new TypeNode(new QualifiedName("Boolean")));
			p.setAstNode(n);

		});

		getNonTerminal("stringLiteral").addSemanticAction( (p, r) -> {

			StringValue n = new StringValue();
			n.setType(new TypeNode(new QualifiedName("String")));
			p.setAstNode(n);

		});

		getNonTerminal("nullLiteral").addSemanticAction( (p, r) -> {

			NullValue n = new NullValue();
			n.setType(new TypeNode(new QualifiedName("Any")));

			p.setAstNode(n);
			
		});


		getNonTerminal("superDeclaration").addSemanticAction( (p, r) -> {
			if (r.get(1).getSemanticAttribute("lexicalValue").isPresent()){
				QualifiedName n = new QualifiedName();

				n.append((String)r.get(1).getSemanticAttribute("lexicalValue").get());

				p.setSemanticAttribute("node", n);
			} else {
				p.setSemanticAttribute("node",r.get(1).getSemanticAttribute("node").get());		
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
	private BooleanOperatorNode.Operation resolveBooleanOperation(Symbol symbol) {

		String op = (String)((Symbol)symbol.getParserTreeNode().getChildren().get(0)).getSemanticAttribute("lexicalValue").get();

		switch (op) {
		case "&":
			return BooleanOperatorNode.Operation.BitAnd;
		case "&&":
			return BooleanOperatorNode.Operation.ShortAnd;
		case "|":
			return BooleanOperatorNode.Operation.BitOr;
		case "||":
			return BooleanOperatorNode.Operation.ShortOr;
		case "^":
			return BooleanOperatorNode.Operation.BitXor;
		case "~":
			return BooleanOperatorNode.Operation.BitNegate;
		case "!":
			return BooleanOperatorNode.Operation.Negate;
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


	public static ArithmeticNode.Operation resolveOperation(Symbol symbol){

		String op = (String)((Symbol)symbol.getParserTreeNode().getChildren().get(0)).getSemanticAttribute("lexicalValue").get();

		switch (op) {
		case "+":
			return ArithmeticNode.Operation.Addition;
		case "++":
			return ArithmeticNode.Operation.Increment;
		case "--":
			return ArithmeticNode.Operation.Decrement;
		case "-":
			return ArithmeticNode.Operation.Subtraction;
		case "*":
			return ArithmeticNode.Operation.Multiplication;
		case "/":
			return ArithmeticNode.Operation.Division;
		case "%":
			return ArithmeticNode.Operation.Remainder;
		case "//":
			return ArithmeticNode.Operation.FractionDivision;
		case ">>":
			return ArithmeticNode.Operation.RightShift;
		case "<<":
			return ArithmeticNode.Operation.LeftShift;
		case ">>>":
			return ArithmeticNode.Operation.RightPositiveShift;
		default:
			throw new RuntimeException(op + "is not a recognized operator");
		}

	}


}
