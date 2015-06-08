/**
 * 
 */
package compiler.bnf;

import java.util.ListIterator;

import compiler.syntax.AstNode;
import compiler.trees.Node;
import compiler.trees.Visitor;

/**
 * 
 */
public class OptionalTransformVisitor implements Visitor<AstNode> {

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
		ListIterator<AstNode> it =  node.getChildren().listIterator();
		
		while(it.hasNext()){
			AstNode n = it.next();
			if ( n instanceof RuleRef){
				RuleRef ref = (RuleRef)n;
				if (ref.getName().endsWith("?")){
					String name = ref.getName().substring(0, ref.getName().length() - 1);
					Rule rule = new Rule();
					String maybeName = "maybe" + name.substring(0, 1).toUpperCase() + name.substring(1);
					rule.setName(maybeName);

					RuleRef notMaybe = new RuleRef();
					notMaybe.setName(name);
					
					RuleRef empty = new RuleRef();
					empty.setName("empty");
					
					RulesAlternative alt  = new RulesAlternative(); 
					alt.add(notMaybe);
					alt.add(empty);
					
					rule.add(alt);
		
					AstNode root = getRoot(n);
					
					if (!root.getChildren().contains(rule)){
						root.add(rule);
					}
					
					
					RuleRef newRef = new RuleRef();
					newRef.setName(maybeName);
					
					it.remove();
					it.add(newRef);
				}
			}
		}
	}

	AstNode root;
	private AstNode getRoot(AstNode n){
		if (root != null){
			return root;
		}
		
		while (n.getParent() != null){
			n = n.getParent();
		}
		root = n;
		
		return root;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitAfterChildren(AstNode node) {
	
	}


}
