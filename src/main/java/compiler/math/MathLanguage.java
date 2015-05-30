/**
 * 
 */
package compiler.math;

import compiler.Language;
import compiler.parser.nodes.ParserTreeNode;
import compiler.syntax.AstNode;

/**
 * 
 */
public class MathLanguage extends Language{

	/**
	 * Constructor.
	 * @param grammar
	 */
	public MathLanguage() {
		super(new MathGrammar());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstNode transform(ParserTreeNode node) {

		AstNode root = new AstNode();

//		if (node.isEmpty()){
//			return null;
//		} else if (node.getChildren().isEmpty()){
//			if (node instanceof IdentifierNode){
//				return new VariableNode(((IdentifierNode)node).getId());
//			} else if (node instanceof NumericNode){
//				return new Value(((NumericNode)node).getLong());
//			} else if (node instanceof TerminalNode ){
//				return null;
//			}
//		} else if (node.getChildren().size() == 1){
//			return transform(node.getChildren().get(0));
//		}  else {
//		
//			List<AstNode> list = new ArrayList<>();
//			for(ParserTreeNode n : node.getChildren())
//			{
//				AstNode b = transform(n);
//				if (b != null){
//					list.add(b);
//				}
//				
//			}
//			
//			NonTerminalNode non = (NonTerminalNode)node;
//			if (non != null && non.getName().equals("sum")){
//				SumNode sum = new SumNode();
//				if (list.size() == 1){
//					sum.add(list.get(0));
//				} else if (list.size() == 2){
//					sum.add(list.get(0));
//					sum.add(list.get(1));
//				} else {
//					throw new RuntimeException("Syntax exception: expected two operands for sum");
//				}
//				return sum;
//			} if (non != null && non.getName().equals("diff")){
//				DiffNode sum = new DiffNode();
//				if (list.size() == 1){
//					sum.add(list.get(0));
//				} else if (list.size() == 2){
//					sum.add(list.get(0));
//					sum.add(list.get(1));
//				} else {
//					throw new RuntimeException("Syntax exception: expected two operands for diff");
//				}
//				return sum;
//			}else if (non != null && non.getName().equals("expr")){
//				if (list.size() == 2){
//					
//					AstNode sum;
//					if (list.get(0) instanceof OperatorNode){
//						sum = list.get(0);
//						sum.add(list.get(1));
//						return sum;
//					} else if (list.get(1) instanceof OperatorNode){
//						sum = list.get(1);
//						sum.addFirst(list.get(0));
//						return sum;
//					}
//					
//				} else {
//					throw new RuntimeException("Syntax exception: expected two operands for Expression");
//				}
//			}
//			
//			if (list.size() == 1){
//				return list.get(0);
//			} else if (list.isEmpty()){
//				return list.get(0);
//			} else {
//				for(AstNode r : list)
//				{
//					root.add(r);
//				}
//			}
//		}
//		
		return root;
	}


}

