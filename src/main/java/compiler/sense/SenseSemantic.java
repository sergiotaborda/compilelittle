/**
 * 
 */
package compiler.sense;

import java.util.ArrayList;
import java.util.List;

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
			}else if (node instanceof TypeNode){
				TypeNode t = (TypeNode)node;
				
				t.setType(semanticContext.typeForName(t.getName()));
			} else if (node instanceof VariableReadNode){
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
			if (node instanceof ScopedVariableDefinitionNode){
				ScopedVariableDefinitionNode variableDeclaration = (ScopedVariableDefinitionNode)node;
				// TODO validate variable name;
				Type type = variableDeclaration.getType();
				VariableInfo info = semanticContext.currentScope().defineVariable(variableDeclaration.getName(), type);
				
				TypedNode init = variableDeclaration.getInicializer();
				
				if (init != null){
					
					info.setInitialized(true);
					Type right = init.getType();
					
					if (!right.isAssignableTo(type)){
						throw new SyntaxError( right + " is not assignable to " + type );
					}
				}

			} else if (node instanceof ArithmeticNode){
				ArithmeticNode n = (ArithmeticNode)node;
				
				Type left = n.getLeft().getType();
				Type right = n.getRight().getType();
				
				if (left.equals(right)){
					n.setType(left);
				} else {
					throw new SyntaxError("Method " + n.getOperation().equivalentMethod() +  "(" + right  + ") is not defined in " + left);
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
				IndexedAccessNode n = (IndexedAccessNode)node;
			}else if (node instanceof MethodInvocationNode){
				MethodInvocationNode m = (MethodInvocationNode)node;
				
				TypedNode a = (TypedNode) m.getAccess();
				String name = m.getCall().getName();
				
				Type type = a.getType();
				
				List<Method> list = type.getAvailableMethods(name);
				
				if (list.isEmpty()){
					throw new SyntaxError("The method " + name + "() is undefined for type " + type);
				} else {
					Method mdth = list.get(0);
					m.setType(mdth.getReturningType());
				}
				
			} else if (node instanceof ReturnNode){
				ReturnNode n = (ReturnNode)node;
				
				semanticContext.currentScope().defineVariable("@returnOfMethod", n.getType());

			} else if (node instanceof MethodNode){
				
				MethodNode m = (MethodNode)node;
				
				if (!m.getReturnType().isVoid()){
					Type returnType = m.getReturnType().getType();

					VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");
					
					if (!variable.getType().isAssignableTo(returnType)){
						throw new SyntaxError(variable.getType() + " is not assignable to " + returnType);
					}

				} else {
					VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");
					
					if (variable != null){
						throw new SyntaxError("Method does not return a value");
					}
				}
				semanticContext.endScope();
				
			}else if (node instanceof ClassType){
				semanticContext.endScope();
			}
		}
		
	}
}
