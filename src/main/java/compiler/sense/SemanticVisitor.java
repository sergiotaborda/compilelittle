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

import compiler.parser.IdentifierNode;
import compiler.sense.ast.ArgumentListNode;
import compiler.sense.ast.ArithmeticNode;
import compiler.sense.ast.ArithmeticOperation;
import compiler.sense.ast.AssignmentNode;
import compiler.sense.ast.BlockNode;
import compiler.sense.ast.CatchOptionNode;
import compiler.sense.ast.ClassInstanceCreation;
import compiler.sense.ast.ClassType;
import compiler.sense.ast.ConditionalStatement;
import compiler.sense.ast.FieldDeclarationNode;
import compiler.sense.ast.FieldOrPropertyAccessNode;
import compiler.sense.ast.ForEachNode;
import compiler.sense.ast.Imutability;
import compiler.sense.ast.IndexedAccessNode;
import compiler.sense.ast.LambdaExpressionNode;
import compiler.sense.ast.LiteralExpressionNode;
import compiler.sense.ast.MethodDeclarationNode;
import compiler.sense.ast.MethodInvocationNode;
import compiler.sense.ast.ParametersListNode;
import compiler.sense.ast.PosExpression;
import compiler.sense.ast.QualifiedNameNode;
import compiler.sense.ast.RangeNode;
import compiler.sense.ast.ReturnNode;
import compiler.sense.ast.ScopedVariableDefinitionNode;
import compiler.sense.ast.SwitchOption;
import compiler.sense.ast.TypeNode;
import compiler.sense.ast.TypedNode;
import compiler.sense.ast.VariableDeclarationNode;
import compiler.sense.ast.VariableReadNode;
import compiler.sense.ast.VariableWriteNode;
import compiler.sense.ast.VariablesList;
import compiler.sense.typesystem.Kind;
import compiler.sense.typesystem.SenseTypeDefinition;
import compiler.sense.typesystem.SenseTypeSystem;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;
import compiler.trees.Visitor;
import compiler.trees.VisitorNext;
import compiler.typesystem.Field;
import compiler.typesystem.Method;
import compiler.typesystem.MethodParameter;
import compiler.typesystem.MethodSignature;
import compiler.typesystem.Property;
import compiler.typesystem.TypeDefinition;
import compiler.typesystem.VariableInfo;

public class SemanticVisitor implements Visitor<AstNode> {

	SemanticContext semanticContext;

	private Map<String , Set<MethodSignature> > defined = new HashMap<String, Set<MethodSignature>>();
	private Map<String , Set<MethodSignature> > expected = new HashMap<String, Set<MethodSignature>>();

	public SemanticVisitor (SemanticContext sc){
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
					throw new CompilationError("Method '" + entry.getKey() + "' is not defined");
				}

				for (MethodSignature expected : entry.getValue()){
					for (MethodSignature s : def){
						if (expected.isPromotableTo(s)){
							continue outter;
						}
					}
				}
				throw new CompilationError("Method '" + entry.getKey() + "' is not defined");
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

			int genericParametersCount = t.getGenerics() == null ? 0 : t.getGenerics().getChildren().size();
			
			Optional<SenseTypeDefinition> maybeMyType =  semanticContext.resolveTypeForName(t.getName(),genericParametersCount).map(u -> (SenseTypeDefinition)u);

			SenseTypeDefinition myType;
			if (!maybeMyType.isPresent()){
				maybeMyType= SenseTypeSystem.getInstance().getForName(t.getName());
				
				if (maybeMyType.isPresent()){
					myType = maybeMyType.get();
				} else {
					myType = new SenseTypeDefinition(t.getName(),Kind.Class, (SenseTypeDefinition)SenseTypeSystem.Any());
				}
				semanticContext.registerType(myType,genericParametersCount);
			} else {
				myType = (SenseTypeDefinition) maybeMyType.get();
			}
			((SenseTypeDefinition)myType).setKind(t.getKind());
			
			if (t.getGenerics() != null){

				for(AstNode n : t.getGenerics().getChildren()){
					TypeNode tn = (TypeNode)n;
					semanticContext.currentScope().defineTypeVariable(tn.getName(), SenseTypeSystem.Any()); // TODO  T extends X
				}

			}

			
			TypeNode superTypeNode = t.getSuperType();
			TypeDefinition superType = SenseTypeSystem.Any();
			if (superTypeNode != null){
				superType = semanticContext.typeForName(superTypeNode.getName());
				
//				if (superType.isGeneric()){
//					
//					for(AstNode p : superTypeNode.getChildren()){
//						ParametricTypesNode param = (ParametricTypesNode)p;
//						
//						
//					}
//					
//				}
				
				superTypeNode.setTypeDefinition(superType);
				
				
			}
			myType.setSuperTypeDefinition(superType);


			
			TreeTransverser.tranverse(t,new StructureVisitor(myType, semanticContext));
			
			semanticContext.currentScope().defineVariable("this", myType).setInitialized(true);
			semanticContext.currentScope().defineVariable("super", superType).setInitialized(true);
			
		}else if (node instanceof BlockNode){
			semanticContext.beginScope("block");
		} else if (node instanceof VariableReadNode){
			VariableReadNode v = (VariableReadNode)node;
			VariableInfo variableInfo = semanticContext.currentScope().searchVariable(v.getName());
			if (variableInfo == null){
				throw new CompilationError("Variable " + v.getName() + " was not defined");
			}
			if (!variableInfo.isInitialized()){
				throw new CompilationError("Variable " + v.getName() + " was not initialized.");
			}
			v.setVariableInfo(variableInfo);


		} else if (node instanceof VariableWriteNode){
			VariableWriteNode v = (VariableWriteNode)node;
			VariableInfo variableInfo = semanticContext.currentScope().searchVariable(v.getName());
			if (variableInfo == null){
				throw new CompilationError("Variable " + v.getName() + " was not defined");
			}
			variableInfo.markWrite();
			v.setVariableInfo(variableInfo);
		}else if (node instanceof TypeNode){
			TypeNode t = (TypeNode)node;
			t.setTypeDefinition( semanticContext.typeForName(t.getName(), t.getGenericParametersCount()));
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
			TypeDefinition type = t.getTypeDefinition();
			if (t.getChildren().size() > 0){
				
				List<TypeDefinition> generics = new ArrayList<>();
				for(AstNode p : t.getChildren().get(0).getChildren()){
					TypeNode generic = ensureTypeNode(p);
					generics.add(generic.getTypeDefinition());
				}
				
				type = SenseTypeSystem.getInstance().specify( type, generics.toArray(new TypeDefinition[generics.size()]));
				
				t.setTypeDefinition(type);

			}

		} else if (node instanceof RangeNode){
			RangeNode r = (RangeNode)node;

			TypeDefinition left = ((TypedNode)r.getChildren().get(0)).getTypeDefinition();
			TypeDefinition right = ((TypedNode)r.getChildren().get(1)).getTypeDefinition();

			TypeDefinition finalType;
			if (left.equals(right)){
				finalType = left;
			} else if (SenseTypeSystem.getInstance().isPromotableTo(left, right)){
				finalType = right;
			} else if (SenseTypeSystem.getInstance().isPromotableTo(right, left)){
				finalType = left;
			} else {
				throw new CompilationError("Cannot range from " + left + " to " + right);
			}

			r.setTypeDefinition(SenseTypeSystem.getInstance().specify(SenseTypeSystem.Progression(), finalType));
		} else if (node instanceof LambdaExpressionNode){
			LambdaExpressionNode n = (LambdaExpressionNode)node;
			
			int parametersCount = n.getParameters().getChildren().size();
			
			List<TypeDefinition> generics = new ArrayList<>();
			
			generics.add(n.getBody().getTypeDefinition());
			
			for (AstNode v : n.getParameters().getChildren()){
				
				generics.add(((TypedNode)v).getTypeDefinition());
			}
			
			TypeDefinition funtionType = SenseTypeSystem.getInstance().specify(SenseTypeSystem.Function(generics.size()), generics.toArray(new TypeDefinition[generics.size()]));
			
			n.setTypeDefinition(funtionType);
		} else if (node instanceof ArithmeticNode){
			ArithmeticNode n = (ArithmeticNode)node;

			TypeDefinition left = n.getLeft().getTypeDefinition();
			TypeDefinition right = n.getRight().getTypeDefinition();

			if (left.equals(right)){
				n.setTypeDefinition(left);
			} else {
				// find instance operator method

				MethodSignature signature = new MethodSignature(n.getOperation().equivalentMethod(), new MethodParameter(right));
				
				Optional<Method> method = left.getMethodBySignature(signature);

				if (!method.isPresent()){
					
					method = left.getMethodByPromotableSignature(signature);

					if (!method.isPresent()){
						// search static operator
						throw new CompilationError("Method " + n.getOperation().equivalentMethod() +  "(" + right  + ") is not defined in " + left);
					} else {
						// TODO promote
					}
				} 
				// else , sustitute the current node by a mehtod invocation node
				// TODO
				n.setTypeDefinition(method.get().getReturningType().getUpperbound());

			}
		} else if (node instanceof PosExpression){
			PosExpression p = (PosExpression)node;

			if (p.getOperation().equals(ArithmeticOperation.Subtraction)){


				final TypeDefinition type = ((TypedNode)p.getChildren().get(0)).getTypeDefinition();
				Optional<Method> list = type.getMethodsByName("negative").stream().filter(md -> md.getParameters().size() == 0).findAny();

				if (!list.isPresent()){
					throw new CompilationError("The method negative() is undefined for TypeDefinition " + type);
				} 

				p.setTypeDefinition(list.get().getReturningType().getUpperbound());
			}

		} else if (node instanceof AssignmentNode){
			AssignmentNode n = (AssignmentNode)node;

			TypeDefinition left = n.getLeft().getTypeDefinition();
			TypeDefinition right = n.getRight().getTypeDefinition();

			if (!SenseTypeSystem.getInstance().isAssignableTo(right, left)){
				
				if (!SenseTypeSystem.getInstance().isPromotableTo(right, left) /*|| right.isPrimitive()*/){
					throw new CompilationError( right + " is not assignable to " + left );
				} else {
					final MethodInvocationNode m = new  MethodInvocationNode((AstNode)n.getRight() , "to" + left.getSimpleName());
					m.setTypeDefinition(left);
					n.replace( (AstNode)n.getRight(), m);
				}
			}

			if (n.getLeft() instanceof VariableWriteNode){
				VariableInfo info = semanticContext.currentScope().searchVariable(((VariableWriteNode)n.getLeft()).getName());

				if (info.isImutable() && info.isInitialized()){
					throw new CompilationError ("Cannot modify the value of an imutable variable or field");
				}
				info.setInitialized(true);
			} else if (n.getLeft() instanceof FieldOrPropertyAccessNode){
				VariableInfo info = semanticContext.currentScope().searchVariable(((FieldOrPropertyAccessNode)n.getLeft()).getName());

				if (info.isImutable() && info.isInitialized()){
					throw new CompilationError ("Cannot modify the value of an imutable variable or field");
				}
				info.setInitialized(true);
			}

		} else if (node instanceof ScopedVariableDefinitionNode){
			ScopedVariableDefinitionNode variableDeclaration = (ScopedVariableDefinitionNode)node;
			TypeDefinition type = variableDeclaration.getTypeDefinition();
			
			
			
			VariableInfo info = semanticContext.currentScope().defineVariable(variableDeclaration.getName(), type);
			info.setImutable(variableDeclaration.getImutabilityValue() == Imutability.Imutable );
			
			variableDeclaration.setInfo(info);

			TypedNode init = variableDeclaration.getInitializer();

			if (init != null){

				info.setInitialized(true);
				TypeDefinition right = init.getTypeDefinition();

				if (!SenseTypeSystem.getInstance().isAssignableTo(right, type)){
					if (SenseTypeSystem.getInstance().isPromotableTo(right , type) /*|| right.isPrimitive()*/){
						MethodInvocationNode m = new  MethodInvocationNode((AstNode)variableDeclaration.getInitializer() , "to" + type.getSimpleName());
						m.setTypeDefinition(type);
						variableDeclaration.setInitializer(m);
						
					} else {
						throw new CompilationError( right + " is not assignable to variable " + info.getName() + " of TypeDefinition "  + type );
					}	
				}
			}
			
			if (node instanceof FieldDeclarationNode){
				FieldDeclarationNode f = (FieldDeclarationNode)node;
				VariableInfo currentInfo = semanticContext.currentScope().searchVariable("this");
				SenseTypeDefinition currentType = (SenseTypeDefinition) currentInfo.getTypeDefinition();

				currentType.addField(f.getName(), f.getTypeDefinition(), f.getImutabilityValue());
			}


		}else if (node instanceof IndexedAccessNode){
			IndexedAccessNode m = (IndexedAccessNode)node;

			TypedNode a = (TypedNode) m.getAccess();

			VariableInfo info = semanticContext.currentScope().searchVariable("this");
			TypeDefinition currentType = info.getTypeDefinition();

			TypeDefinition methodOwnerType = currentType;
			if (a != null){
				methodOwnerType = a.getTypeDefinition();
			}

			if (methodOwnerType.equals(currentType)){
				// TODO 
			} else {
				Optional<Method> list = methodOwnerType.getMethodsByName("get").stream().filter(md -> md.getParameters().size() == 1).findAny();

				if (!list.isPresent()){
					throw new CompilationError("The method get(" + m.getIndexExpression().getTypeDefinition() + ") is undefined for TypeDefinition " + methodOwnerType);
				} 

				m.setTypeDefinition(list.get().getReturningType().getUpperbound());
			}
		}else if (node instanceof PosExpression){
			PosExpression n = (PosExpression)node;
			n.setTypeDefinition(((TypedNode)n.getChildren().get(0)).getTypeDefinition());

		}else if (node instanceof FieldOrPropertyAccessNode){
			FieldOrPropertyAccessNode m = (FieldOrPropertyAccessNode)node;
			
			VariableInfo info = semanticContext.currentScope().searchVariable("this");
			TypeDefinition currentType = info.getTypeDefinition();
			
			TypeDefinition fieldOwnerType = currentType;

			String name = m.getName();

			AstNode access = m.getPrimary();
		
			
			if (access == null && name.contains(".")){
				access = new QualifiedNameNode(name);
			} 
			
			if (access == null){
				// ok, analise after
			} else if (access instanceof QualifiedNameNode){
				QualifiedNameNode qn = ((QualifiedNameNode)access); 

				Optional<TypeDefinition> maybeType = semanticContext.resolveTypeForName(qn.getName(), 0);

				while(!maybeType.isPresent()){
					qn = qn.getPrevious();
					if (qn != null){
						maybeType = semanticContext.resolveTypeForName((qn).getName(), 0);
					} else {
						break;
					}
				}

				if (maybeType.isPresent()){
					fieldOwnerType = maybeType.get();
					qn = ((QualifiedNameNode)access); 

					Deque<String> path = new LinkedList<>();
					while(qn.getPrevious() != null){
						path.add(qn.getLast().getName());
						qn = qn.getPrevious();

					}

					while (!path.isEmpty()){
						String fieldName = path.pop();
						Optional<Field> maybeField = fieldOwnerType.getFieldByName(fieldName);
						
						if (!maybeField.isPresent()){

							Optional<Property> props = fieldOwnerType.getPropertyByName(fieldName);
							
							if (!props.isPresent()){
								throw new CompilationError( name + " is not a field or a property of TypeDefinition " + fieldOwnerType);
							} else {
								Property property = props.get();
								m.setTypeDefinition(property.getReturningType().getUpperbound());
								
								// REplace PropertyAccess
								
								return;
							}
						} else {
							Field field = maybeField.get();
							m.setTypeDefinition(field.getReturningType());
							
							// REplace FieldAccess
							
							return;
						}
					}

				} else {
					// try variable
					VariableInfo variable = semanticContext.currentScope().searchVariable(((QualifiedNameNode) access).getName());
					
					if (variable == null){
						throw new CompilationError(((QualifiedNameNode) access).getName() + " variable is not defined");
					}
					
					// Replace Variable Read
					
					fieldOwnerType = variable.getTypeDefinition();
			
				}
			} else if (access instanceof TypedNode){
				fieldOwnerType = ((TypedNode)access).getTypeDefinition();
			} else if (access instanceof IdentifierNode){
				
				VariableInfo variable = semanticContext.currentScope().searchVariable(((IdentifierNode) access).getId());

				fieldOwnerType = variable.getTypeDefinition();
			} else {
				throw new CompilationError( access.getClass() + " Not supported yet");
			}

			if (fieldOwnerType.equals(currentType)){
				
				Optional<Field> field = fieldOwnerType.getFieldByName(name);

				if (!field.isPresent()){
					
					// try variable
					VariableInfo variable = semanticContext.currentScope().searchVariable(name);
					
					if (variable == null){
						throw new CompilationError(name + " variable is not defined");
					}
					
					m.setTypeDefinition(variable.getTypeDefinition());
					
					m.getParent().replace(m, new VariableReadNode(name, variable));
				} else {
					m.setTypeDefinition(field.get().getReturningType());
				}
			
			} else {
				
				Optional<Field> field = fieldOwnerType.getFieldByName(name);

				if (!field.isPresent()){
					if (!SenseTypeSystem.getInstance().isAssignableTo(fieldOwnerType, SenseTypeSystem.Maybe())){
						throw new CompilationError("The field " + name + " is not defined for TypeDefinition " + fieldOwnerType);
					}
				
					TypeDefinition innerType = fieldOwnerType.getGenericParameters().get(0).getUpperbound();
					
					field = innerType.getFieldByName(name);

					if (!field.isPresent()){
						throw new CompilationError("The field " + name + "( is not defined for TypeDefinition " + fieldOwnerType);
					}
					
					// transform to call inside the maybe using map
					TypeDefinition finalType = SenseTypeSystem.getInstance().specify(SenseTypeSystem.Maybe(), field.get().getReturningType());
					TypeDefinition mappingFunction = SenseTypeSystem.getInstance().specify(SenseTypeSystem.Function(2),innerType, field.get().getReturningType());
					
					MethodInvocationNode transform = new MethodInvocationNode(
							m.getPrimary(), 
							"map", 
							new ClassInstanceCreation(mappingFunction ) // TODO lambda 
					);
					
					m.getParent().replace(m, transform); // this operation will nullify the transform.type.
					transform.setTypeDefinition(finalType); // set it again
					
				} else {
					m.setTypeDefinition(field.get().getReturningType());
				}
			}
		}else if (node instanceof MethodInvocationNode){
			MethodInvocationNode m = (MethodInvocationNode)node;

			if (m.getTypeDefinition() != null){
				return;
			}
			VariableInfo info = semanticContext.currentScope().searchVariable("this");
			TypeDefinition currentType = info.getTypeDefinition();

			TypeDefinition methodOwnerType = currentType;

			String name = m.getCall().getName();

			AstNode access = m.getAccess();
		
			if (access == null){
				// access to  self
				MethodParameter[] parameters = m.getCall().getArgumentListNode().asMethodParameters();
				MethodSignature signature = new MethodSignature( name, parameters);
				
				Optional<Method> method = methodOwnerType.getMethodBySignature(signature);

				if (!method.isPresent()){
					
					 method = methodOwnerType.getMethodByPromotableSignature(signature);

					if (!method.isPresent()){
						throw new CompilationError("Method " + name + " is not defined in " + methodOwnerType + " or its super classes");
					} 
				} 
					
				m.setTypeDefinition(method.get().getReturningType().getUpperbound());
				
				
				
			} else if (access instanceof QualifiedNameNode){
				QualifiedNameNode qn = ((QualifiedNameNode)access); 

				Optional<TypeDefinition> maybeType = semanticContext.resolveTypeForName(qn.getName(), 0);

				while(!maybeType.isPresent()){
					qn = qn.getPrevious();
					if (qn != null){
						maybeType = semanticContext.resolveTypeForName((qn).getName(), 0);
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
						Optional<Field> maybeField = methodOwnerType.getFieldByName(fieldName);
						
						if (!maybeField.isPresent()){

							throw new CompilationError("The field " + name + " is undefined for TypeDefinition " + methodOwnerType);
						} else {
							Field field = maybeField.get();
							methodOwnerType = field.getReturningType();
						}
					}

				} else {
					// try variable
					String varName = ((QualifiedNameNode) access).getName();
					VariableInfo variableInfo = semanticContext.currentScope().searchVariable(varName);
					
					if (variableInfo == null){
						throw new CompilationError(((QualifiedNameNode) access).getName() + " is not a valid TypeDefinition or object");
					}
					
					VariableReadNode vnode = new VariableReadNode(varName);
					vnode.setVariableInfo(variableInfo);
					
					access.getParent().replace(access, vnode);
					
					methodOwnerType = variableInfo.getTypeDefinition();
			
				}
			} else if (access instanceof TypedNode){
				methodOwnerType = ((TypedNode)access).getTypeDefinition();
			} else if (access instanceof IdentifierNode){
				
				VariableInfo variable = semanticContext.currentScope().searchVariable(((IdentifierNode) access).getId());

				methodOwnerType = variable.getTypeDefinition();
			} else {
				throw new CompilationError("Not supported yet");
			}

			if (methodOwnerType.equals(currentType)){
				markToFind(methodOwnerType, name, m.getCall().getArgumentListNode());
			} else {
				MethodParameter[] parameters = m.getCall().getArgumentListNode().asMethodParameters();
				MethodSignature signature = new MethodSignature( name, parameters);
				
				Optional<Method> method = methodOwnerType.getMethodBySignature(signature);

				if (!method.isPresent()){
							 
					 method = methodOwnerType.getMethodByPromotableSignature(signature);

					if (method.isPresent()){
						// TODO
						 m.setTypeDefinition(method.get().getReturningType().getUpperbound());
					} else {
						if (!SenseTypeSystem.getInstance().isAssignableTo (methodOwnerType, SenseTypeSystem.Maybe())){
							throw new CompilationError("The method " + name + "(" + Arrays.toString(parameters) + ") is undefined for TypeDefinition " + methodOwnerType);
						}
					
						TypeDefinition innerType = methodOwnerType.getGenericParameters().get(0).getUpperbound();
						
						method = innerType.getMethodBySignature(signature);

						if (!method.isPresent()){
							throw new CompilationError("The method " + name + "(" + Arrays.toString(parameters) + ") is undefined for TypeDefinition " + innerType);
						}
						
						// transform to call inside the maybe using map
						TypeDefinition innerCallReturn = method.get().getReturningType().getUpperbound();
						TypeDefinition finalType = SenseTypeSystem.getInstance().specify(SenseTypeSystem.Maybe(), innerCallReturn);
						
						TypeDefinition functionType = SenseTypeSystem.getInstance().specify(SenseTypeSystem.Function(2), innerType, innerCallReturn);
						
						MethodSignature mapSignature = new MethodSignature(
								"map", 
								new MethodParameter(functionType, "it")
						);
						
						Optional<Method> mapMethod = finalType.getMethodBySignature(mapSignature);
						
						MethodInvocationNode transform = new MethodInvocationNode(
								m.getAccess(), 
								"map", 
								new ClassInstanceCreation(functionType)
						);
						
						m.getParent().replace(m, transform);
						transform.setTypeDefinition(finalType);
					}
					
					
				} else {
					Method mdth =method.get();
					m.setTypeDefinition(mdth.getReturningType().getUpperbound());
				}
			}

		} else if (node instanceof ReturnNode){
			ReturnNode n = (ReturnNode)node;

			if (semanticContext.currentScope().getParent() == null){
				throw new RuntimeException("Cannot exist return in master scope");
			}

			if (!n.getChildren().isEmpty() && (n.getChildren().get(0) instanceof VariableReadNode)){
				VariableReadNode vr = (VariableReadNode)n.getChildren().get(0);

				semanticContext.currentScope().searchVariable(vr.getName()).markEscapes();
			}

			// define variable in the method scope. the current scope is block
			semanticContext.currentScope().getParent().defineVariable("@returnOfMethod", n.getTypeDefinition());

		} else if (node instanceof MethodDeclarationNode){

			MethodDeclarationNode m = (MethodDeclarationNode)node;
			
			VariableInfo var = semanticContext.currentScope().searchVariable("this");

			if (var.getTypeDefinition().getKind() == Kind.Interface){
				m.setAbstract(true);
			}
			
			markDefine(var.getTypeDefinition(), m.getName(), m.getParameters());
			if (m.getReturnType().isVoid()){
				VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");

				if (variable != null){
					throw new CompilationError("Method " + m.getName() + " can not return a value");
				}

			} else if (!m.isAbstract()){

				TypeDefinition returnType = m.getReturnType().getTypeDefinition();
				if (!returnType.equals(SenseTypeSystem.Void())){
					VariableInfo variable = semanticContext.currentScope().searchVariable("@returnOfMethod");

					if (variable == null){
						throw new CompilationError("Method " +  m.getName() +" must return a result of TypeDefinition " + returnType);
					}

					if (!SenseTypeSystem.getInstance().isAssignableTo(variable.getTypeDefinition(), returnType)){
						
						if (!SenseTypeSystem.getInstance().isPromotableTo(variable.getTypeDefinition(), returnType)){
							throw new CompilationError(variable.getTypeDefinition() + " is not assignable to " + returnType + " in the return of method " + m.getName() );
						} else {
							// TODO promote
						}
						
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
					if (tn.getTypeDefinition().getKind() != Kind.Interface){
						throw new CompilationError(t.getName() + " cannot implement TypeDefinition " + tn.getTypeDefinition().getName() + " because " + tn.getTypeDefinition().getName() + " it is not an interface");
					}
				}

				// TODO verify methods are implemented
			}
		} else if (node instanceof ConditionalStatement){

			if (!((ConditionalStatement)node).getCondition().getTypeDefinition().equals(SenseTypeSystem.Boolean())){
				throw new CompilationError("Condition must be a Boolean value");
			}
		}  else if (node instanceof ForEachNode){
			ForEachNode n = (ForEachNode)node;

			if (!SenseTypeSystem.getInstance().isAssignableTo(n.getContainer().getTypeDefinition(),SenseTypeSystem.Iterable())){
				throw new CompilationError("Can only iterate over an instance of " + SenseTypeSystem.Iterable());
			}

			if (!SenseTypeSystem.getInstance().isAssignableTo(n.getContainer().getTypeDefinition().getGenericParameters().get(0).getUpperbound() , n.getVariableDeclarationNode().getTypeDefinition())){
				throw new CompilationError(n.getVariableDeclarationNode().getTypeDefinition().getSimpleName() + " is not contained in " + n.getContainer().getTypeDefinition());
			}

		} else if (node instanceof ParametersListNode){

			for (AstNode n : node.getChildren()){
				VariableDeclarationNode var = (VariableDeclarationNode)n;
				// mark this variables as initialized as they are parameters
				semanticContext.currentScope().searchVariable(var.getName()).setInitialized(true);
			}
		} else if (node instanceof CatchOptionNode){

			TypeDefinition exceptionType = ((CatchOptionNode)node).getExceptions().getTypeDefinition();
			if (!SenseTypeSystem.getInstance().isAssignableTo(exceptionType, SenseTypeSystem.Exception())){
				throw new CompilationError("No exception of TypeDefinition " + exceptionType.getSimpleName() + " can be thrown; an exception TypeDefinition must be a subclass of Exception");
			}

		} else if (node instanceof SwitchOption){


			final SwitchOption s = (SwitchOption)node;
			if (!s.isDefault()){
				boolean literal = s.getValue() instanceof LiteralExpressionNode;
				if (!literal){
					throw new CompilationError("Switch option must be a constant");
				}
			}


		} else if (node instanceof BlockNode){
			semanticContext.endScope();
		}
	}

	/**
	 * @param p
	 * @return
	 */
	private TypeNode ensureTypeNode(AstNode p) {
		if (p instanceof TypeNode){
			return (TypeNode)p;
		} else {
			return ((compiler.sense.ast.ParametricTypesNode)p).getTypeNode();
		}
	}

	/**
	 * @param name
	 * @param parameters
	 */
	private void markDefine(TypeDefinition TypeDefinition , String name, ParametersListNode parameters) {
		Set<MethodSignature> signatures = defined.get(name);
		if (signatures == null){
			signatures = new HashSet<>();
			defined.put(name, signatures);
		}

		MethodParameter[] params = parameters == null ? new MethodParameter[0] : new MethodParameter[parameters.getChildren().size()];
		for (int i = 0; i < params.length; i++){
			VariableDeclarationNode var = (VariableDeclarationNode)parameters.getChildren().get(i);
			params[i] = new MethodParameter(var.getTypeDefinition());
		}
		final MethodSignature methodSignature = new MethodSignature(name, params);

		signatures.add(methodSignature);
	}

	/**
	 * @param name
	 * @param argumentListNode
	 */
	private void markToFind(TypeDefinition declaringType ,String name, ArgumentListNode arguments) {
		Set<MethodSignature> signatures = expected.get(name);
		if (signatures == null){
			signatures = new HashSet<>();
			expected.put(name, signatures);
		}

		MethodParameter[] params =  arguments == null ? new MethodParameter[0] : new MethodParameter[arguments.getChildren().size()];
		for (int i = 0; i < params.length; i++){
			TypedNode var = (TypedNode)arguments.getChildren().get(i);
			params[i] = new MethodParameter(var.getTypeDefinition());
		}
		final MethodSignature methodSignature = new MethodSignature(name,params);
		signatures.add(methodSignature);
	}

}