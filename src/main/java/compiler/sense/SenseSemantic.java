/**
 * 
 */
package compiler.sense;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import compiler.sense.typesystem.Method;
import compiler.sense.typesystem.Type;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;
import compiler.trees.Visitor;

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
			for (AstNode n : imports.getChildren()){
				sc.addImportPackage(((ImportNode)n).getName().getName());
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
		public void visitBeforeChildren(AstNode node) {
			if (node instanceof MethodNode){
				semanticContext.beginScope();
			}else if (node instanceof ClassType){
				semanticContext.beginScope();
				
				ClassType t = (ClassType)node;
				
				TypeNode st = t.getSuperType();
				Type superType = Type.Any;
				if (st != null){
					superType = semanticContext.typeForName(st.getName());
					st.setType(superType);
				}

				semanticContext.currentScope().defineVariable("this", new Type(t.getName()));
				semanticContext.currentScope().defineVariable("super", superType);
			}else if (node instanceof BlockNode){
				semanticContext.beginScope();
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
						type.addParameter(g);
					}
				}
				
		
			} else if (node instanceof ScopedVariableDefinitionNode){
				ScopedVariableDefinitionNode variableDeclaration = (ScopedVariableDefinitionNode)node;
				Type type = variableDeclaration.getType();
				VariableInfo info = semanticContext.currentScope().defineVariable(variableDeclaration.getName(), type);

				TypedNode init = variableDeclaration.getInitializer();

				if (init != null){

					info.setInitialized(true);
					Type right = init.getType();

					if (!right.isAssignableTo(type)){
						if (right.isPromotableTo(type)){
							variableDeclaration.setInitializer(new PromoteNode((ExpressionNode)variableDeclaration.getInitializer(), type));
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

					Optional<Method> method = left.getAvailableMethods(n.getOperation().equivalentMethod()).stream()
							.filter(md -> md.getParameters().size() == 1 && md.getParameters().get(0).getType().equals(right))
							.findAny();
						

					if (!method.isPresent()){
						// search static operator
						throw new SyntaxError("Method " + n.getOperation().equivalentMethod() +  "(" + right  + ") is not defined in " + left);
					} 
					// else , sustitute the current node by a mehtod invocation node

					n.setType(method.get().getReturningType());
					
				}
			} else if (node instanceof PosArithmeticUnaryExpression){
				PosArithmeticUnaryExpression p = (PosArithmeticUnaryExpression)node;
				
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

				TypedNode a = (TypedNode) m.getAccess();
				String name = m.getCall().getName();

				VariableInfo info = semanticContext.currentScope().searchVariable("this");
				Type currentType = info.getType();

				Type methodOwnerType = currentType;
				if (a != null){
					methodOwnerType = a.getType();
				}

				if (methodOwnerType.equals(currentType)){
					// TODO 
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

				semanticContext.currentScope().defineVariable("@returnOfMethod", n.getType());

			} else if (node instanceof MethodNode){

				MethodNode m = (MethodNode)node;

				if (m.getReturnType().isVoid()){
					VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");

					if (variable != null){
						throw new SyntaxError("Method " + m.getName() + " can not return a value");
					}

				} else {

					Type returnType = m.getReturnType().getType();

					VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");

					if (!variable.getType().isAssignableTo(returnType)){
						throw new SyntaxError(variable.getType() + " is not assignable to " + returnType);
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
				
				boolean literal = ((SwitchOption)node).getValue() instanceof LiteralExpressionNode;
				if (!literal){
					throw new SyntaxError("Switch option must be a constant");
				}

			} else if (node instanceof BlockNode){
				semanticContext.endScope();
			}
		}

	}
}
