/**
 * 
 */
package compiler.sense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import compiler.sense.typesystem.Method;
import compiler.sense.typesystem.ConcreteMethodParameter;
import compiler.sense.typesystem.MethodSignature;
import compiler.sense.typesystem.SenseType;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;
import compiler.trees.Visitor;
import compiler.trees.VisitorNext;
import compiler.typesystem.Field;
import compiler.typesystem.Type;
import compiler.typesystem.TypesRepository;

/**
 * 
 */
public class SenseSemantic {


	private TypesRepository repository;

	public SenseSemantic(TypesRepository repository){
		this.repository = repository;
	}
	/**
	 * @param t
	 */
	public void analise(UnitTypes t) {


		List<ClassType> classes = new ArrayList<>(2);
		ImportsNode imports = null;
		for(AstNode n : t.getChildren()){
			if (n instanceof ClassType){
				classes.add((ClassType)n);
			} else {
				imports = (ImportsNode)n;
			}
		}

		SemanticContext sc = new SemanticContext(repository);

		if (imports != null){
			for(AstNode n : imports.getChildren()){
				ImportNode i = (ImportNode)n;

				// try for class
				Type u = sc.typeForName(i.getName().getName());
				if (u == null){
					// add to names
					sc.addImportPackage(i.getName().getName());
				} else {
					// add type package to packages
					sc.addImportPackage(i.getName().getPrevious().getName());
				}
			}
		}



		for (ClassType ct : classes){



			VariablesVisitor vv = new VariablesVisitor(sc);
			TreeTransverser.tranverse(ct, vv);
		}
	}



	public static class VariablesVisitor implements Visitor<AstNode> {

		SemanticContext semanticContext;

		private Map<String , Set<MethodSignature> > defined = new HashMap<String, Set<MethodSignature>>();
		private Map<String , Set<MethodSignature> > expected = new HashMap<String, Set<MethodSignature>>();

		public VariablesVisitor (SemanticContext sc){
			this.semanticContext = sc;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void startVisit() {

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void endVisit() {

			if (!expected.isEmpty()){
				outter: for(Map.Entry<String, Set<MethodSignature>> entry : expected.entrySet()){
					Set<MethodSignature> def = defined.get(entry.getKey());

					if (def == null || def.isEmpty()){
						throw new SyntaxError("Method '" + entry.getKey() + "' is not defined");
					}

					for (MethodSignature exp : entry.getValue()){
						for (MethodSignature s : def){
							if (exp.isAssignableTo(s)){
								continue outter;
							}
						}
					}
					throw new SyntaxError("Method '" + entry.getKey() + "' is not defined");
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public VisitorNext visitBeforeChildren(AstNode node) {

			if (node instanceof MethodDeclarationNode){
				semanticContext.beginScope(((MethodDeclarationNode)node).getName());
			} else if (node instanceof ClassType){
				ClassType t = (ClassType)node;

				semanticContext.beginScope(t.getName());

				TypeNode st = t.getSuperType();
				Type superType = SenseType.Any;
				if (st != null){
					superType = semanticContext.typeForName(st.getName());
					st.setType(superType);
				}

				ClassType ct = (ClassType)node;

				if (ct.getGenerics() != null){

					for(AstNode n : ct.getGenerics().getChildren()){
						TypeNode tn = (TypeNode)n;
						semanticContext.currentScope().defineTypeVariable(tn.getName(), SenseType.Any); // TODO  T extends X
					}

				}



				semanticContext.currentScope().defineVariable("this", new SenseType(t.getName())).setInitialized(true);
				semanticContext.currentScope().defineVariable("super", superType).setInitialized(true);
			}else if (node instanceof BlockNode){
				semanticContext.beginScope("block");
			}else if (node instanceof TypeNode){
				TypeNode t = (TypeNode)node;
				t.setType( semanticContext.typeForName(t.getName()));
			}  else if (node instanceof VariableReadNode){
				VariableReadNode v = (VariableReadNode)node;
				VariableInfo variableInfo = semanticContext.currentScope().searchVariable(v.getName());
				if (variableInfo == null){
					throw new SyntaxError("Variable " + v.getName() + " was not defined");
				}
				if (!variableInfo.isInitialized()){
					throw new SyntaxError("Variable " + v.getName() + " was not initialized.");
				}
				v.setVariableInfo(variableInfo);


			} else if (node instanceof VariableWriteNode){
				VariableWriteNode v = (VariableWriteNode)node;
				VariableInfo variableInfo = semanticContext.currentScope().searchVariable(v.getName());
				if (variableInfo == null){
					throw new SyntaxError("Variable " + v.getName() + " was not defined");
				}
				variableInfo.markWrite();
				v.setVariableInfo(variableInfo);
			}

			return VisitorNext.Children;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void visitAfterChildren(AstNode node) {
			if (node instanceof TypeNode){
				TypeNode t = (TypeNode)node;
				Type type = t.getType();
				if (t.getChildren().size() > 0){
					for(AstNode p : t.getChildren().get(0).getChildren()){
						TypeNode generic = (TypeNode)p;
						Type g = generic.getType();
						type = type.of(g);
					}
					t.setType(type);

				}

			} if ( node instanceof ClassInstanceCreation){
				ClassInstanceCreation a = (ClassInstanceCreation)node;
			} else if (node instanceof RangeNode){
				RangeNode r = (RangeNode)node;

				Type left = ((TypedNode)r.getChildren().get(0)).getType();
				Type right = ((TypedNode)r.getChildren().get(1)).getType();

				Type finalType;
				if (left.equals(right)){
					finalType = left;
				} else if (left.isPromotableTo(right)){
					finalType = right;
				} else if (right.isPromotableTo(left)){
					finalType = left;
				} else {
					throw new SyntaxError("Cannot range from " + left + " to " + right);
				}

				r.type = SenseType.Progression.of(finalType);


			}else if (node instanceof ScopedVariableDefinitionNode){
				ScopedVariableDefinitionNode variableDeclaration = (ScopedVariableDefinitionNode)node;
				Type type = variableDeclaration.getType();
				VariableInfo info = semanticContext.currentScope().defineVariable(variableDeclaration.getName(), type);
				info.setImutable(variableDeclaration.getImutability() == Imutability.Imutable );
				
				variableDeclaration.setInfo(info);

				TypedNode init = variableDeclaration.getInitializer();

				if (init != null){

					info.setInitialized(true);
					Type right = init.getType();

					if (!right.isAssignableTo(type)){
						if (right.isPromotableTo(type) /*|| right.isPrimitive()*/){
							variableDeclaration.setInitializer(new PromoteNode((ExpressionNode)variableDeclaration.getInitializer(),right, type));
						} else {
							throw new SyntaxError( right + " is not assignable to variable " + info.getName() + " of type "  + type );
						}	
					}
				}

			} else if (node instanceof ArithmeticNode){
				ArithmeticNode n = (ArithmeticNode)node;

				Type left = n.getLeft().getType();
				Type right = n.getRight().getType();

				if (left.equals(right)){
					n.setType(left);
				} else {
					// find instance operator method

					Optional<Method> method = left.getAppropriateMethod(new MethodSignature(left, n.getOperation().equivalentMethod(), new ConcreteMethodParameter(right)));

					if (!method.isPresent()){
						// search static operator
						throw new SyntaxError("Method " + n.getOperation().equivalentMethod() +  "(" + right  + ") is not defined in " + left);
					} 
					// else , sustitute the current node by a mehtod invocation node
					// TODO
					n.setType(method.get().getReturningType());

				}
			} else if (node instanceof PosExpression){
				PosExpression p = (PosExpression)node;

				if (p.getOperation().equals(ArithmeticOperation.Subtraction)){


					final Type type = ((TypedNode)p.getChildren().get(0)).getType();
					Optional<Method> list = type.getAvailableMethods("negative").stream().filter(md -> md.getParameters().size() == 0).findAny();

					if (!list.isPresent()){
						throw new SyntaxError("The method negative() is undefined for type " + type);
					} 

					p.setType(list.get().getReturningType());
				}

			} else if (node instanceof AssignmentNode){
				AssignmentNode n = (AssignmentNode)node;

				Type left = n.getLeft().getType();
				Type right = n.getRight().getType();

				if (!right.isAssignableTo(left)){
					throw new SyntaxError( right + " is not assignable to " + left );
				}

				if (n.getLeft() instanceof VariableWriteNode){
					VariableInfo info = semanticContext.currentScope().searchVariable(((VariableWriteNode)n.getLeft()).getName());

					if (info.isImutable() && info.isInitialized()){
						throw new SyntaxError ("Cannot modify the value of an imutable variable or field");
					}
					info.setInitialized(true);
				}
			} else if (node instanceof FieldAccessNode){
				FieldAccessNode n = (FieldAccessNode)node;


			}else if (node instanceof IndexedAccessNode){
				IndexedAccessNode m = (IndexedAccessNode)node;

				TypedNode a = (TypedNode) m.getAccess();

				VariableInfo info = semanticContext.currentScope().searchVariable("this");
				Type currentType = info.getType();

				Type methodOwnerType = currentType;
				if (a != null){
					methodOwnerType = a.getType();
				}

				if (methodOwnerType.equals(currentType)){
					// TODO 
				} else {
					Optional<Method> list = methodOwnerType.getAvailableMethods("get").stream().filter(md -> md.getParameters().size() == 1).findAny();

					if (!list.isPresent()){
						throw new SyntaxError("The method get(" + m.getIndexExpression().getType() + ") is undefined for type " + methodOwnerType);
					} 

					m.setType(list.get().getReturningType());
				}
			}else if (node instanceof PosExpression){
				PosExpression n = (PosExpression)node;
				n.setType(((TypedNode)n.getChildren().get(0)).getType());

			}else if (node instanceof MethodInvocationNode){
				MethodInvocationNode m = (MethodInvocationNode)node;

				VariableInfo info = semanticContext.currentScope().searchVariable("this");
				Type currentType = info.getType();

				Type methodOwnerType = currentType;

				String name = m.getCall().getName();

				AstNode access = m.getAccess();
				Type accessType;
				if (access == null){
					// acces to  self
					markToFind(currentType, name, m.getCall().getArgumentListNode());
				} else if (access instanceof QualifiedNameNode){
					QualifiedNameNode qn = ((QualifiedNameNode)access); 

					Optional<Type> maybeType = semanticContext.resolveTypeForName(qn.getName());

					while(!maybeType.isPresent()){
						qn = qn.getPrevious();
						if (qn != null){
							maybeType = semanticContext.resolveTypeForName((qn).getName());
						} else {
							break;
						}
					}

					if (maybeType.isPresent()){
						methodOwnerType = maybeType.get();
						qn = ((QualifiedNameNode)access); 

						Deque<String> path = new LinkedList<>();
						while(qn.getPrevious() != null){
							path.add(qn.getLast().getName());
							qn = qn.getPrevious();

						}

						while (!path.isEmpty()){
							String fieldName = path.pop();
							List<Field> list = methodOwnerType.getAvailableFields(fieldName);
							
							if (list.isEmpty()){

								throw new SyntaxError("The field " + name + " is undefined for type " + methodOwnerType);
							} else {
								Field field = list.get(0);
								methodOwnerType = field.getType();
							}
						}

					} else {
						// try variable
						VariableInfo variable = semanticContext.currentScope().searchVariable(((QualifiedNameNode) access).getName());
						
						if (variable == null){
							throw new SyntaxError(((QualifiedNameNode) access).getName() + " is not a valid type or object");
						}
						
						methodOwnerType = variable.getType();
				
					}
				} else if (access instanceof TypedNode){
					methodOwnerType = ((TypedNode)access).getType();
				} else {
					throw new SyntaxError("Not supported yet");
				}

				if (methodOwnerType.equals(currentType)){
					// TODO 
					//throw new RuntimeException("self access to methods not implemented yet");
				} else {
					ConcreteMethodParameter[] parameters = m.getCall().getArgumentListNode().asMethodParameters();
					MethodSignature signature = new MethodSignature(methodOwnerType, name, parameters);
					
					Optional<Method> method = methodOwnerType.getAppropriateMethod(signature);

					if (!method.isPresent()){

						throw new SyntaxError("The method " + name + "(" + Arrays.toString(parameters) + ") is undefined for type " + methodOwnerType);
					} else {
						Method mdth =method.get();
						m.setType(mdth.getReturningType());
					}
				}

			} else if (node instanceof ReturnNode){
				ReturnNode n = (ReturnNode)node;

				if (semanticContext.currentScope().getParent() == null){
					throw new RuntimeException("Cannot exist return in master scope");
				}

				if (n.getChildren().get(0) instanceof VariableReadNode){
					VariableReadNode vr = (VariableReadNode)n.getChildren().get(0);

					semanticContext.currentScope().searchVariable(vr.getName()).markEscapes();
				}

				// define variable in the method scope. the current scope is block
				semanticContext.currentScope().getParent().defineVariable("@returnOfMethod", n.getType());

			} else if (node instanceof MethodDeclarationNode){

				MethodDeclarationNode m = (MethodDeclarationNode)node;
				VariableInfo var = semanticContext.currentScope().searchVariable("this");

				markDefine(var.getType(), m.getName(), m.getParameters());
				if (m.getReturnType().isVoid()){
					VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");

					if (variable != null){
						throw new SyntaxError("Method " + m.getName() + " can not return a value");
					}

				} else {

					Type returnType = m.getReturnType().getType();
					if (!returnType.equals(SenseType.Void)){
						VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");

						if (variable == null){
							throw new SyntaxError("Method " +  m.getName() +" must return a result of type " + returnType);
						}

						if (!variable.getType().isAssignableTo(returnType)){
							throw new SyntaxError(variable.getType() + " is not assignable to " + returnType + " in the return of method " + m.getName() );
						}
					}
					

				}


				semanticContext.endScope();

			}else if (node instanceof ClassType){
				semanticContext.endScope();
				ClassType t = (ClassType)node;
				if (t.getInterfaces() != null){

					for(AstNode n : t.getInterfaces().getChildren()){
						TypeNode tn = (TypeNode)n;
						if (tn.getType().getKind() != Kind.Interface){
							throw new SyntaxError(t.getName() + " cannot implement type " + tn.getType().getName() + " because " + tn.getType().getName() + " it is not an interface");
						}
					}

					// TODO verify methods are implemented
				}
			} else if (node instanceof ConditionalStatement){

				if (!((ConditionalStatement)node).getCondition().getType().equals(SenseType.Boolean)){
					throw new SyntaxError("Condition must be a Boolean value");
				}
			}  else if (node instanceof ForEachNode){
				ForEachNode n = (ForEachNode)node;

				if (!n.getContainer().getType().isAssignableTo(SenseType.Iterable)){
					throw new SyntaxError("Can only iterate over an instance of " + SenseType.Iterable);
				}

				if (!n.getContainer().getType().getParameters().get(0).isAssignableTo(n.getVariableDeclarationNode().getType())){
					throw new SyntaxError(n.getVariableDeclarationNode().getType().getSimpleName() + " is not contained in " + n.getContainer().getType());
				}

			} else if (node instanceof ParametersListNode){

				for (AstNode n : node.getChildren()){
					VariableDeclarationNode var = (VariableDeclarationNode)n;
					// mark this variables as initialized as they are parameters
					semanticContext.currentScope().searchVariable(var.getName()).setInitialized(true);
				}
			} else if (node instanceof CatchOptionNode){

				Type exceptionType = ((CatchOptionNode)node).getExceptions().getType();
				if (!exceptionType.isAssignableTo(SenseType.Exception)){
					throw new SyntaxError("No exception of type " + exceptionType.getSimpleName() + " can be thrown; an exception type must be a subclass of Exception");
				}

			} else if (node instanceof SwitchOption){


				final SwitchOption s = (SwitchOption)node;
				if (!s.isDefault()){
					boolean literal = s.getValue() instanceof LiteralExpressionNode;
					if (!literal){
						throw new SyntaxError("Switch option must be a constant");
					}
				}


			} else if (node instanceof BlockNode){
				semanticContext.endScope();
			}
		}

		/**
		 * @param name
		 * @param parameters
		 */
		private void markDefine(Type type , String name, ParametersListNode parameters) {
			Set<MethodSignature> signatures = defined.get(name);
			if (signatures == null){
				signatures = new HashSet<>();
				defined.put(name, signatures);
			}

			ConcreteMethodParameter[] params = parameters == null ? new ConcreteMethodParameter[0] : new ConcreteMethodParameter[parameters.getChildren().size()];
			for (int i = 0; i < params.length; i++){
				VariableDeclarationNode var = (VariableDeclarationNode)parameters.getChildren().get(i);
				params[i] = new ConcreteMethodParameter(var.getType());
			}
			final MethodSignature methodSignature = new MethodSignature(type, name, params);

			signatures.add(methodSignature);
		}

		/**
		 * @param name
		 * @param argumentListNode
		 */
		private void markToFind(Type type ,String name, ArgumentListNode arguments) {
			Set<MethodSignature> signatures = expected.get(name);
			if (signatures == null){
				signatures = new HashSet<>();
				expected.put(name, signatures);
			}

			ConcreteMethodParameter[] params =  arguments == null ? new ConcreteMethodParameter[0] : new ConcreteMethodParameter[arguments.getChildren().size()];
			for (int i = 0; i < params.length; i++){
				TypedNode var = (TypedNode)arguments.getChildren().get(i);
				params[i] = new ConcreteMethodParameter(var.getType());
			}
			final MethodSignature methodSignature = new MethodSignature(type, name,params);
			signatures.add(methodSignature);
		}

	}
}
