/**
 * 
 */
package compiler.sense;

import compiler.sense.ast.ArithmeticNode;
import compiler.sense.ast.AssignmentNode;
import compiler.sense.ast.BooleanValue;
import compiler.sense.ast.ClassInstanceCreation;
import compiler.sense.ast.ExpressionNode;
import compiler.sense.ast.FieldOrPropertyAccessNode;
import compiler.sense.ast.LiteralExpressionNode;
import compiler.sense.ast.MethodInvocationNode;
import compiler.sense.ast.NullValue;
import compiler.sense.ast.NumericValue;
import compiler.sense.ast.ScopedVariableDefinitionNode;
import compiler.sense.ast.StringConcatenationNode;
import compiler.sense.ast.StringValue;
import compiler.sense.typesystem.SenseTypeSystem;
import compiler.syntax.AstNode;
import compiler.trees.Visitor;
import compiler.trees.VisitorNext;

/**
 * 
 */
public class LiteralsInstanciatorVisitor implements Visitor<AstNode> {

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
		return VisitorNext.Children;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitAfterChildren(AstNode node) {
		
		if (node instanceof AssignmentNode){
			AssignmentNode n = (AssignmentNode) node;
			if (n.getRight() instanceof LiteralExpressionNode){
				n.setRight(transformeLiteral((LiteralExpressionNode)n.getRight()));
			}
		} else if (node instanceof ScopedVariableDefinitionNode){
			ScopedVariableDefinitionNode v = (ScopedVariableDefinitionNode)node;
			
			if (v.getInitializer() instanceof LiteralExpressionNode){
				v.setInitializer(transformeLiteral((LiteralExpressionNode)v.getInitializer()));
			}
			
		} 
		if (node instanceof LiteralExpressionNode){
			AstNode newnode = transformeLiteral((LiteralExpressionNode) node);
			
			if (node.getParent().getParent() instanceof ClassInstanceCreation){
				ClassInstanceCreation c = (ClassInstanceCreation)node.getParent().getParent();
				if (c.getTypeDefinition().equals(SenseTypeSystem.String())){
					return;
				}
			} 
			node.getParent().replace(node, newnode);
		}
		else if (node instanceof ArithmeticNode){
			ArithmeticNode a = (ArithmeticNode) node;
			if (a.getTypeDefinition().getName().equals("sense.String")){
				StringConcatenationNode c;
				if (a.getLeft() instanceof StringConcatenationNode){
					c = (StringConcatenationNode)a.getLeft();
					c.add(a.getRight());
				} else {
					c= new StringConcatenationNode();
					c.add(a.getLeft());
					c.add(a.getRight());
					
				}
				a.getParent().replace(a, c);		
				
			}
		}else if (node instanceof MethodInvocationNode){
			MethodInvocationNode m = (MethodInvocationNode) node;
			if (m.getAccess() instanceof NumericValue){
				AstNode newnode = transformeLiteral((LiteralExpressionNode) m.getAccess());
				m.replace(m.getAccess(), newnode);
			}
		}
	}

	private ExpressionNode transformeLiteral(LiteralExpressionNode literal) {
	
		if (literal instanceof BooleanValue){
			return literal;
			//new FieldAccessNode(((BooleanValue)literal).isValue()? "True" : "False");
		} else if (literal instanceof NullValue){
			FieldOrPropertyAccessNode n = new FieldOrPropertyAccessNode("None.None");
			n.setTypeDefinition(SenseTypeSystem.None());
			return n;
		} else if (literal instanceof NumericValue){
			return new ClassInstanceCreation(literal.getTypeDefinition(), 
					new ClassInstanceCreation(SenseTypeSystem.String(), new StringValue(literal.getLiteralValue())));
		} else {
			return new ClassInstanceCreation(literal.getTypeDefinition(), new StringValue(literal.getLiteralValue()));
		}
	}




}
