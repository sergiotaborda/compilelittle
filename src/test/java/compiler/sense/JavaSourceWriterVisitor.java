/**
 * 
 */
package compiler.sense;

import java.io.PrintWriter;

import compiler.sense.typesystem.Type;
import compiler.sense.typesystem.TypeParameter;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;
import compiler.trees.Visitor;
import compiler.trees.VisitorNext;

/**
 * 
 */
public class JavaSourceWriterVisitor implements Visitor<AstNode>  {

	private PrintWriter writer;

	/**
	 * Constructor.
	 * @param writer
	 */
	public JavaSourceWriterVisitor(PrintWriter writer) {
		this.writer = writer;
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
		writer.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VisitorNext visitBeforeChildren(AstNode node) {
		try {
			if (node instanceof StringValue){
				writer.print("\"");
				writer.print(((StringValue)node).getValue());
				writer.print("\"");
			} else if (node instanceof BooleanValue){
				writer.print(((BooleanValue)node).isValue() ? "true" : "false");
			} else if (node instanceof ClassType){
				ClassType t = (ClassType)node;

				int pos = t.getName().lastIndexOf('.');
				if (pos >0){
					writer.print("package ");
					writer.print(t.getName().substring(0, pos));
					writer.print(";\n");
				}

				writer.println();

				if (t.getAnnotationListNode()!=null){
					for(AstNode n : t.getAnnotationListNode().getChildren()){
						AnnotationNode anot = (AnnotationNode)n;
						writer.print(anot.getName());
						writer.print(" ");
					}
				}

				writer.print("class ");
				writer.print(t.getName().substring(pos+1));

				if (t.getSuperType() != null){
					writer.print(" extends ");
					writer.print(t.getSuperType().getName());

				}

				writer.println("{");
				TreeTransverser.tranverse(t.getBody(), new JavaSourceWriterVisitor(writer));
				writer.println("}");

				return VisitorNext.Siblings;
			} else if (node instanceof TryStatement ){
				TryStatement t = (TryStatement)node;

				writer.print("try");

				if (t.getResource() != null){
					writer.print("(");
					TreeTransverser.tranverse(t.getResource(), new JavaSourceWriterVisitor(writer));
					writer.print(")");
				}

				TreeTransverser.tranverse(t.getInstructions(), new JavaSourceWriterVisitor(writer));

				if (t.getCatchOptions() != null){
					for (AstNode a : t.getCatchOptions().getChildren()){
						CatchOptionNode c = (CatchOptionNode)a;

						writer.print(" catch (");

						TreeTransverser.tranverse(c.getExceptions(), new JavaSourceWriterVisitor(writer));

						writer.println(") {");

						TreeTransverser.tranverse(c.getInstructions(), new JavaSourceWriterVisitor(writer));

						writer.println("}");
					}
				}

				if (t.getfinalInstructions() != null){
					writer.print(" finally (");
					TreeTransverser.tranverse(t.getResource(), new JavaSourceWriterVisitor(writer));
					writer.print(")");
				}

				return VisitorNext.Siblings;
			} else if (node instanceof RangeNode ){
				RangeNode r = (RangeNode)node;
				
				writer.print(" new IntProgressable(");
				TreeTransverser.tranverse(r.getStart(), new JavaSourceWriterVisitor(writer));
				writer.print(",");
				TreeTransverser.tranverse(r.getEnd(), new JavaSourceWriterVisitor(writer));
				writer.print(")");
				return VisitorNext.Siblings;
			} else if (node instanceof ForEachNode ){
				ForEachNode f = (ForEachNode)node;

				writer.print(" for (");

				visitBeforeChildren(f.getVariableDeclarationNode());
				writer.print(" : ");

				TreeTransverser.tranverse(f.getContainer(), new JavaSourceWriterVisitor(writer));
				writer.println(")");

				TreeTransverser.tranverse(f.getStatements(), new JavaSourceWriterVisitor(writer));

				return VisitorNext.Siblings;
			} else if (node instanceof WhileNode ){
				WhileNode f = (WhileNode)node;

				writer.println();
				writer.print(" while (");
				
				TreeTransverser.tranverse(f.getCondition(), new JavaSourceWriterVisitor(writer));
				
				writer.println(")");

				TreeTransverser.tranverse(f.getStatements(), new JavaSourceWriterVisitor(writer));

				return VisitorNext.Siblings;
			} else if (node instanceof SwitchNode ){
				SwitchNode n = (SwitchNode)node;

				writer.println();
				writer.print(" switch (");
				TreeTransverser.tranverse(n.getCandidate(), new JavaSourceWriterVisitor(writer));
				writer.println(") { ");

				for (AstNode a : n.getSwitchOptions().getChildren()){
					SwitchOption op = (SwitchOption)a;
					if (op.isDefault()){
						writer.print(" default");
					} else {
						writer.print(" case (");
						TreeTransverser.tranverse(op.getValue(), new JavaSourceWriterVisitor(writer));
						writer.println(")");
					}
	
					TreeTransverser.tranverse(op.getActions(), new JavaSourceWriterVisitor(writer));
					
				}
			
				writer.println("}");
				
				return VisitorNext.Siblings;
			} else if (node instanceof DecisionNode ){
				DecisionNode n = (DecisionNode)node;

				if (n.getCondition() != null){
					writer.print("if (");	
					TreeTransverser.tranverse(n.getCondition(), new JavaSourceWriterVisitor(writer));	
					writer.println(")");

				}
			
				TreeTransverser.tranverse(n.getTrueBlock(), new JavaSourceWriterVisitor(writer));

				if (n.getFalseBlock() != null){
					writer.print(" else ");
					TreeTransverser.tranverse(n.getFalseBlock(), new JavaSourceWriterVisitor(writer));

				}
				return VisitorNext.Siblings;
			} else if (node instanceof MethodInvocationNode ){
				MethodInvocationNode n = (MethodInvocationNode)node;

				if (n.getAccess() != null){
					TreeTransverser.tranverse(n.getAccess(), new JavaSourceWriterVisitor(writer));
					writer.print(".");
				}
				
				writer.print(n.getCall().getName());
				writer.print("(");
				TreeTransverser.tranverse(n.getCall().getArgumentListNode(), new JavaSourceWriterVisitor(writer));
				writer.print(")");
				
				if (node.getParent() instanceof BlockNode){
					writer.println(";");
				}
				
				return VisitorNext.Siblings;
			} else if (node instanceof AssignmentNode){
				AssignmentNode n = (AssignmentNode)node;
				
				TreeTransverser.tranverse((AstNode)n.getLeft(), new JavaSourceWriterVisitor(writer));
				writer.print(" = ");
				TreeTransverser.tranverse(n.getRight(), new JavaSourceWriterVisitor(writer));
				return VisitorNext.Siblings;
			} else if (node instanceof ClassInstanceCreation){
				ClassInstanceCreation n = (ClassInstanceCreation)node;
				
				writer.print(" new ");
				writer.print(n.getType().getName());
				writer.print(" (");
				TreeTransverser.tranverse(n.getArgumentList(), new JavaSourceWriterVisitor(writer));
				writer.print(")");

				return VisitorNext.Siblings;
			}  else if (node instanceof ArgumentListNode){
				ArgumentListNode n = (ArgumentListNode)node;
				
				boolean first = true;
				for(AstNode a : n.getChildren()){
					if (!first){
						writer.print(",");
					} else {
						first = false;
					}
					TreeTransverser.tranverse(a, new JavaSourceWriterVisitor(writer));
				
				}
		

				return VisitorNext.Siblings;
			} else if (node instanceof ContinueNode ){
				ContinueNode f = (ContinueNode)node;

				writer.println("continue;");
				
				return VisitorNext.Siblings;
			} else if (node instanceof TypeNode ){

					TypeNode t = (TypeNode)node;
					if (t.isVoid()){
						writer.print("void");
					} else {
						final Type type = t.getType();
						writer.print(type.getSimpleName());
						
						writeGenerics(type);
					}
					
					writer.print(" ");
					return VisitorNext.Siblings;
			} else if (node instanceof VariableDeclarationNode ){
				VariableDeclarationNode t = (VariableDeclarationNode)node;
				writer.print(t.getType().getName());
				writer.print(" ");
				writer.print(t.getName());
				
				if (t.getInitializer() != null){
					writer.print(" = ");
					TreeTransverser.tranverse(t.getInitializer(), new JavaSourceWriterVisitor(writer));
				}

				if (node.getParent() instanceof BlockNode){
					writer.println(";");
				}
				return VisitorNext.Siblings;
			} else if (node instanceof VariableWriteNode ){
				VariableWriteNode t = (VariableWriteNode)node;
				writer.print(t.getName());
				
				return VisitorNext.Siblings;
			} else if (node instanceof VariableReadNode ){
				VariableReadNode t = (VariableReadNode)node;
				writer.print(t.getName());
			} else if (node instanceof BlockNode ){
				writer.print("{\n");
			} else if (node instanceof ReturnNode ){
				writer.print("return ");
			} else if (node instanceof NumericValue ){
				NumericValue n = (NumericValue)node;
				writer.print(n.toString());

			} else if (node instanceof AssignmentNode){
				AssignmentNode n = (AssignmentNode)node;

				visitBeforeChildren((AstNode)n.getLeft());
				writer.print(" ");
				writer.print(n.getOperation().symbol());
				writer.print(" ");
				visitBeforeChildren(n.getRight());
				writer.print(";\n");
				return VisitorNext.Siblings;
			} else if (node instanceof PosExpression ){
				PosExpression n = (PosExpression)node;

				writer.print(n.getOperation().symbol());
	

			} else if (node instanceof PosUnaryExpression ){
				PosUnaryExpression n = (PosUnaryExpression)node;

				writer.print(n.getOperation().symbol());
				

			}  else if (node instanceof ArithmeticNode ){
				ArithmeticNode n = (ArithmeticNode)node;

				TreeTransverser.tranverse(n.getLeft(), new JavaSourceWriterVisitor(writer));

				writer.print(" ");
				writer.print(n.getOperation().symbol());
				writer.print(" ");

				TreeTransverser.tranverse(n.getRight(), new JavaSourceWriterVisitor(writer));

				
				return VisitorNext.Siblings;
			} else if (node instanceof BooleanOperatorNode ){
				BooleanOperatorNode n = (BooleanOperatorNode)node;

				TreeTransverser.tranverse(n.getLeft(), new JavaSourceWriterVisitor(writer));

				writer.print(" ");
				writer.print(n.getOperation().symbol());
				writer.print(" ");

				TreeTransverser.tranverse(n.getRight(), new JavaSourceWriterVisitor(writer));
				
				
				return VisitorNext.Siblings;
			} else if (node instanceof ComparisonNode ){
				ComparisonNode n = (ComparisonNode)node;

				TreeTransverser.tranverse(n.getLeft(), new JavaSourceWriterVisitor(writer));

				writer.print(" ");
				writer.print(n.getOperation().symbol());
				writer.print(" ");

				TreeTransverser.tranverse(n.getRight(), new JavaSourceWriterVisitor(writer));
	
				return VisitorNext.Siblings;
			} else if (node instanceof FieldAccessNode ){
				FieldAccessNode n = (FieldAccessNode)node;
				
				writer.print(n.getName());
				
			}else if (node instanceof IndexedAccessNode ){
				IndexedAccessNode n = (IndexedAccessNode)node;
				
				TreeTransverser.tranverse(n.getAccess(), new JavaSourceWriterVisitor(writer));
				writer.print("[");
				TreeTransverser.tranverse(n.getIndexExpression(), new JavaSourceWriterVisitor(writer));
				writer.print("]");
			} else if (node instanceof MethodNode ){
			
				MethodNode m = (MethodNode)node;

		
				writer.println("\t");

				if ( m.getAnnotationListNode() != null){
					for(AstNode n : m.getAnnotationListNode().getChildren()){
						AnnotationNode anot = (AnnotationNode)n;
						writer.print(anot.getName());
						writer.print(" ");
					}
				}
				TreeTransverser.tranverse(m.getReturnType(), new JavaSourceWriterVisitor(writer));

				writer.print(" ");
				writer.print(m.getName());
				writer.print("(");

				if (m.getParameters() != null){
					boolean isFirst = true;
					for (AstNode n : m.getParameters().getChildren()){
						VariableDeclarationNode p = (VariableDeclarationNode)n;

						if(isFirst){
							isFirst = false;
						} else {
							writer.print(", ");
						}

						TreeTransverser.tranverse(p.getTypeNode(), new JavaSourceWriterVisitor(writer));

						writer.print(" ");
						writer.print(p.getName());



					}
				}

				writer.print(")");
				TreeTransverser.tranverse(m.getBlock(), new JavaSourceWriterVisitor(writer));

				return VisitorNext.Siblings;
			} else if (node instanceof ParametersListNode){
				return VisitorNext.Siblings;
			} else if (node instanceof FieldDeclarationNode ){
				FieldDeclarationNode m = (FieldDeclarationNode)node;

				writer.print("\n");
				writer.print(m.getType().getSimpleName());
				writer.print(" ");
				writer.print(m.getName());


				if (m.getInitializer() != null){
					writer.print(" = ");
					TreeTransverser.tranverse(m.getInitializer(), new JavaSourceWriterVisitor(writer));	
				}
				writer.print(";");
				return VisitorNext.Siblings;
			}

			return VisitorNext.Children;
		} finally {
			writer.flush();
		}

	}

	private void writeGenerics(final Type type) {
		if (!type.getParameters().isEmpty()){
			writer.print("<");
			boolean first = true;
			for(TypeParameter p : type.getParameters()){
				if (first){
					first = false;
				} else {
					writer.print(",");
				}
				writer.print(p.getType().getSimpleName());
				writeGenerics(p.getType());
			}
			writer.print(">");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitAfterChildren(AstNode node) {
		if (node instanceof BlockNode ){
			writer.print("}\n");
		} else if (node instanceof ClassType){
			writer.print("}\n");
		} else if (node instanceof StatementNode ){
			writer.print(";\n");
		}
	}

}
