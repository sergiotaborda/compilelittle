package compiler;

import compiler.parser.MatchableProduction;
import compiler.parser.Production;

public interface FirstFollowTable {

	PromisseSet<MatchableProduction> firstOf(Production left);
	RealizedPromisseSet<MatchableProduction> followOf(Production left);
}
