/**
 * 
 */
package compiler.bnf;

import java.util.ListIterator;

import compiler.syntax.AstNode;
import compiler.trees.Visitor;
import compiler.trees.VisitorNext;

/**
 * 
 */
public class OptionalTransformVisitorWithSubstitution implements Visitor<AstNode> {

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
		ListIterator<AstNode> it =  node.getChildren().listIterator();

		while(it.hasNext()){
			AstNode n = it.next();
			if ( n instanceof RuleRef){
				RuleRef ref = (RuleRef)n;
				if (ref.getName().endsWith("?")){

					AstNode parent = n.getParent();

					RulesAlternative alt = optionalize(parent);
					
					if (alt != null){
						AstNode superParent = parent.getParent();
						
						superParent.replace(parent, alt);
					}
				}
			}
		}
		return VisitorNext.Children;
	}

	/**
	 * @param parent
	 */
	private RulesAlternative optionalize(AstNode parent) {
		if (parent == null){
			return null;
		}
		if (parent instanceof RulesSequence){
			
			RulesSequence seq = (RulesSequence)parent;

			for (AstNode s : seq.getChildren()){

				if ( s instanceof RuleRef){
					RuleRef ref = ((RuleRef)s);

					if(((RuleRef)s).getName().endsWith("?")){
						RulesAlternative alt  = new RulesAlternative(); 
						String name = ref.getName().substring(0, ref.getName().length() - 1);
						RuleRef notMaybe = new RuleRef();
						notMaybe.setName(name);


						RulesSequence a = seq.duplicate();
						RulesSequence b = seq.duplicate();
						// create sequence with s
						a.replace(s, notMaybe);

						// create sequence without 

						b.remove(s);
						
						RulesAlternative altA = optionalize(a);
						RulesAlternative altB = optionalize(b);
						alt.add(altA == null ? a : altA);
						alt.add(altB == null ? b : altB);
						
						return alt;
						
					}
				}
			}

			return null;
		} else {
			throw new RuntimeException();
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
