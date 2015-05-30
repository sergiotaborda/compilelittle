/**
 * 
 */
package compiler.math;

import compiler.Language;
import compiler.math.ast.DiffNode;
import compiler.math.ast.OperatorNode;
import compiler.math.ast.SumNode;
import compiler.math.ast.Value;
import compiler.math.ast.VariableNode;
import compiler.parser.nodes.ParserTreeNode;
import compiler.parser.nodes.TerminalNode;
import compiler.syntax.AstNode;

/**
 * 
 */
public class MathLanguage2 extends Language{

	/**
	 * Constructor.
	 * @param grammar
	 */
	public MathLanguage2() {
		super(new MathGrammar2());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstNode transform(ParserTreeNode node) {

//		if (node.isEmpty()){
//			return null;
//		} else if (node instanceof IdentifierNode){
//			return new VariableNode(((IdentifierNode)node).getId());
//		} else if (node instanceof NumericNode){
//			return new Value(((NumericNode)node).getLong());
//		} else if (node.isTerminal()){
//			return null;
//		}  else if (node.isNonTerminal()){
//			NonTerminalNode non = (NonTerminalNode)node;
//
//			if (non.getName().equals("nopr")){
//
//				Value vn = (Value) transform(node.getChildren().get(0));
//				if (node.getChildren().size() == 1){
//					return vn;
//				}
//				return parseOperator(node, vn);
//
//			} else if (non.getName().equals("vopr")){
//				VariableNode vn = (VariableNode) transform(node.getChildren().get(0));
//				if (node.getChildren().size() == 1){
//					return vn;
//				}
//				return parseOperator(node, vn);
//			} else if (non.getName().equals("expr")){
//				if (node.getChildren().size() == 3){
//					return transform(node.getChildren().get(1));
//				} else {
//					return transform(node.getChildren().get(0));
//				}
//			}
//		} else {
//			if (node.getChildren().size() == 1){
//				return transform(node.getChildren().get(0));
//			}
//
//			AstNode root = new AstNode();
//			for(ParserTreeNode t : node.getChildren()){
//				root.add(transform(t));
//			}
//			return root;
//		}
		return null;

	}

	private OperatorNode parseOperator(ParserTreeNode node, AstNode vn) {
		TerminalNode t = (TerminalNode)node.getChildren().get(1).getChildren().get(0);

		OperatorNode op;
		if (t.getText().equals("+")){
			op = new SumNode();
		} else if (t.getText().equals("-")){
			op = new DiffNode();
		} else {
			op = new SumNode();
		}
		op.add(vn);
		AstNode p = transform(node.getChildren().get(2));
		if (p instanceof Value || p instanceof VariableNode){
			op.add(p);
		} else if (p instanceof OperatorNode){
			op.add(p);
		}
		return op;
	}


}

