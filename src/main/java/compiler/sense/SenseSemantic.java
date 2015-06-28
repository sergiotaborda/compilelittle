/**
 * 
 */
package compiler.sense;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import compiler.sense.typesystem.Method;
import compiler.sense.typesystem.MethodParameter;
import compiler.sense.typesystem.MethodSignature;
import compiler.sense.typesystem.Type;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;
import compiler.trees.Visitor;
import compiler.trees.VisitorNext;

/**
 * 
 */
public class SenseSemantic {

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

		SemanticContext sc = new SemanticContext();

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
					sc.addImportPackage(i.getName().getPrevious());
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
				Type superType = Type.Any;
				if (st != null){
					superType = semanticContext.typeForName(st.getName());
					st.setType(superType);
				}
				
				ClassType ct = (ClassType)node;
				
				if (ct.getGenerics() != null){
					
					for(AstNode n : ct.getGenerics().getChildren()){
						TypeNode tn = (TypeNode)n;
						semanticContext.currentScope().defineTypeVariable(tn.getName(), Type.Any); // TODO  T extends X
					}
					
				}
				

				semanticContext.currentScope().defineVariable("this", new Type(t.getName())).setInitialized(true);
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
				
				r.type = Type.Progression.of(finalType);
				
		
			}else if (node instanceof ScopedVariableDefinitionNode){
				ScopedVariableDefinitionNode variableDeclaration = (ScopedVariableDefinitionNode)node;
				Type type = variableDeclaration.getType();
				VariableInfo info = semanticContext.currentScope().defineVariable(variableDeclaration.getName(), type);

				TypedNode init = variableDeclaration.getInitializer();

				if (init != null){

					info.setInitialized(true);
					Type right = init.getType();

					if (!right.isAssignableTo(type)){
						if (right.isPromotableTo(type) || right.isPrimitive()){
							variableDeclaration.setInitializer(new PromoteNode((ExpressionNode)variableDeclaration.getInitializer(),right, type));
						} else {
							throw new SyntaxError( right + " is not assignable to " + type );
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

					Optional<Method> method = left.getAppropriateMethod(new MethodSignature(left, n.getOperation().equivalentMethod(), new MethodParameter(right)));

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

			}else if (node instanceof MethodInvocationNode){
				MethodInvocationNode m = (MethodInvocationNode)node;

				VariableInfo info = semanticContext.currentScope().searchVariable("this");
				Type currentType = info.getType();
				
				Type methodOwnerType = currentType;
				
				AstNode access = m.getAccess();
				Type accessType;
				if (access == null){
					// acces to  self
				} else if (access instanceof QualifiedNameNode){
					accessType = semanticContext.typeForName(((QualifiedNameNode) access).getName());
					if (accessType != null){
						methodOwnerType = accessType;
					}
				} else if (access instanceof TypedNode){
					methodOwnerType = ((TypedNode)access).getType();
				} else {
					throw new SyntaxError("Not supported yet");
				}
			
				String name = m.getCall().getName();

		

				if (methodOwnerType.equals(currentType)){
					// TODO 
					//throw new RuntimeException("self access to methods not implemented yet");
				} else {
					List<Method> list = methodOwnerType.getAvailableMethods(name);

					if (list.isEmpty()){

						throw new SyntaxError("The method " + name + "() is undefined for type " + methodOwnerType);
					} else {
						Method mdth = list.get(0);
						m.setType(mdth.getReturningType());
					}
				}


			} else if (node instanceof ReturnNode){
				ReturnNode n = (ReturnNode)node;

				if (semanticContext.currentScope().getParent() == null){
					throw new RuntimeException("Cannot exist return in master scope");
				}
				// define variable in the method scope. the current scope is block
				semanticContext.currentScope().getParent().defineVariable("@returnOfMethod", n.getType());

			} else if (node instanceof MethodDeclarationNode){

				MethodDeclarationNode m = (MethodDeclarationNode)node;

				if (m.getReturnType().isVoid()){
					VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");

					if (variable != null){
						throw new SyntaxError("Method " + m.getName() + " can not return a value");
					}

				} else {

					Type returnType = m.getReturnType().getType();
					VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");

					if (variable == null){
						throw new SyntaxError("Method " +  m.getName() +" must return a result of type " + returnType);
					}
					
					if (!variable.getType().isAssignableTo(returnType)){
						throw new SyntaxError(variable.getType() + " is not assignable to " + returnType + " in the return of method " + m.getName() );
					}

				}
				semanticContext.endScope();

			}else if (node instanceof ClassType){
				semanticContext.endScope();
			} else if (node instanceof ConditionalStatement){

				if (!((ConditionalStatement)node).getCondition().getType().equals(Type.Boolean)){
					throw new SyntaxError("Condition must be a Boolean value");
				}
			}  else if (node instanceof ForEachNode){
				ForEachNode n = (ForEachNode)node;
				
				if (!n.getContainer().getType().isAssignableTo(Type.Iterable)){
					throw new SyntaxError("Can only iterate over an instance of " + Type.Iterable);
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
				if (!exceptionType.isAssignableTo(Type.Exception)){
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



	}
}
