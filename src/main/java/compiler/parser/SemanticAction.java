package compiler.parser;

import java.util.List;

public interface SemanticAction {

	void execute(Symbol leftHand, List<Symbol> rightHand);

}
