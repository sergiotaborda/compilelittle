package compiler;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import compiler.parser.EOFTerminal;
import compiler.parser.EmptyTerminal;
import compiler.parser.MatchableProduction;
import compiler.parser.NonTerminal;
import compiler.parser.Production;
import compiler.parser.ProductionSequence;

public class FirstFollowTableCalculator {


	public FirstFollowTable calculateFrom(Production startProduction){

		MappedFirstFollowTable table = new MappedFirstFollowTable();


		Set<NonTerminal> beingCalculated = new HashSet<NonTerminal>();

		table.addFirst(startProduction, calculateFirst(startProduction, table,beingCalculated));


		// follow
		table.addFollow(startProduction, EOFTerminal.instance());
		table.addFollow(startProduction.toNonTerminal().getRule(), EOFTerminal.instance());

		// find each occurence in the right side of each nonterminal

		Map<NonTerminal, Set<NonTerminal>> occurences = new HashMap<>(); 

		beingCalculated = new HashSet<NonTerminal>();
		findOccurences(startProduction.toNonTerminal(), startProduction.toNonTerminal().getRule(), occurences,beingCalculated);

		LinkedList<Pair> rightmost = new LinkedList<>();
		for(Map.Entry<NonTerminal, Set<NonTerminal>> entry : occurences.entrySet()){

			for (NonTerminal p : entry.getValue() ){
				calculateFollow(entry.getKey(), p, table, rightmost);
			}

		}



		Map<Production, FuturePromisseSet<MatchableProduction>> map = new HashMap<>();

		for (Pair p : rightmost){
			FuturePromisseSet<MatchableProduction> left = map.get(p.left);
			if (left == null){
				left = FuturePromisseSet.promisse(table.followOf(p.left));
				map.put(p.left, left);
			}
			FuturePromisseSet<MatchableProduction> right = map.get(p.right);
			if (right == null){
				right = FuturePromisseSet.promisse(table.followOf(p.right));
				map.put(p.right, right);
			}
			right.put(left);

		}

		for( Map.Entry<Production, FuturePromisseSet<MatchableProduction>> m : map.entrySet()){
			PromisseSet<MatchableProduction> set = m.getValue();
			set = set.realise();
			table.addFollow(m.getKey(), set);
		}

		return table;

	}



	/**
	 * @param startProduction
	 * @param occurences
	 */
	private void findOccurences(NonTerminal left , Production rule, Map<NonTerminal, Set<NonTerminal>> occurences,  Set<NonTerminal> beingCalculated) {

		
			if (rule.isTerminal() || rule.isAutoNonTerminal()){
				return;
			} else if (rule.isNonTerminal()){
				final NonTerminal nonTerminal = rule.toNonTerminal();
				addToListMap(occurences, nonTerminal, left );

				if (beingCalculated.add(nonTerminal)){
					findOccurences(nonTerminal, nonTerminal.getRule(), occurences,beingCalculated);
					beingCalculated.remove(nonTerminal);
				}
			}else if (rule.isAlternative()){
				for(Production p : rule.toAlternative()){
					findOccurences(left, p, occurences,beingCalculated);
				}
			} else if (rule.isSequence()){

				for (Production p : rule.toSequence()){
					if (p.isNonTerminal()){
						Set<NonTerminal> set = occurences.get(p);

						if (set == null || !set.contains(left)){
							final NonTerminal nonTerminal = p.toNonTerminal();
							addToListMap(occurences, nonTerminal, left );
							
							if (beingCalculated.add(nonTerminal)){
								findOccurences(nonTerminal, nonTerminal.getRule(), occurences,beingCalculated);
								beingCalculated.remove(nonTerminal);
							}
						}

					} else if (p.isTerminal()){
						// no-op
					} else {
						findOccurences(left, p, occurences, beingCalculated);
					}
				}
			}
		
	}


	private <K,V> void addToListMap(Map<K, Set<V>> map , K n , V p){
		Set<V> list = map.get(n);

		if (list == null){
			list = new HashSet<V>();
			map.put(n, list);
		}

		list.add(p);
	}

	private void scanNonTerminals(Production p, Set<NonTerminal> all) {

		if (p.isTerminal() || p.isAutoNonTerminal()){
			return; 
		} else if (p.isNonTerminal()){
			if (all.add(p.toNonTerminal())){
				scanNonTerminals(p.toNonTerminal().getRule(), all);
			}
		} else if (p.isSequence()){
			for(Production d : p.toSequence()){
				scanNonTerminals(d, all);
			}
		}else if (p.isAlternative()){
			for(Production d : p.toAlternative()){
				scanNonTerminals(d, all);
			}
		}else {
			throw new RuntimeException();
		}
	}

	private void calculateFollow(NonTerminal nonTerminal, NonTerminal containerProduction,MappedFirstFollowTable table, List<Pair> rightmost) {


		Production rule = containerProduction.getRule();
		if (rule.isSequence()){

			ProductionSequence seq = rule.toSequence();

			followSequence(nonTerminal, containerProduction, table, rightmost,seq);
		} else if (rule.isAlternative()){

			Deque<Production> deque = new LinkedList<>();

			for (Production d : rule.toAlternative())
			{
				if (d == nonTerminal){
					// the follow is the follow of the containerProduction left hand side
					rightmost.add(new Pair(containerProduction, nonTerminal));
				} else {
					deque.add(d);
				}
			}

			while (!deque.isEmpty()){
				Production p = deque.removeFirst();
				if (p.isAlternative()){
					for (Production d : p.toAlternative())
					{
						deque.add(d);
					}
				} else if (p.isSequence()){
					followSequence(nonTerminal, containerProduction, table, rightmost, p.toSequence());
				} 
			}
		} else {
			throw new RuntimeException();
		}
	}



	private void followSequence(NonTerminal nonTerminal,
			NonTerminal containerProduction, MappedFirstFollowTable table,
			List<Pair> rightmost, ProductionSequence seq) {
		for (int i = 0; i < seq.size(); i++){
			Production item = seq.get(i);

			if (item.equals(nonTerminal))
			{
				// nonTerminal found in sequence.
				// if is the last one
				if (i == seq.size() - 1)
				{ 
					// the follow is the follow of the containerProduction left hand side
					rightmost.add(new Pair(containerProduction, nonTerminal));
					break;
				}
				else
				{

					inner:while(true) {

						if (i == seq.size() - 1){ // is the last item
							// the follow is the follow of the containerProduction left hand side
							rightmost.add(new Pair(containerProduction, nonTerminal));
							break;
						} else {
							// take the first of the next item
							PromisseSet<MatchableProduction> firstSet = table.firstOf(seq.get(i+1));
							if (firstSet.contains(EmptyTerminal.instance())){
								PromisseSet<MatchableProduction> newSet = firstSet.subtract(EmptyTerminal.instance());
								table.addFollow(nonTerminal, newSet);
								i++;
								if (i == seq.size()){
									break inner;
								}
							} else {
								table.addFollow(nonTerminal, firstSet);
								break inner;
							} 
						}
					} 

				}
			}
		}
	}


	private PromisseSet<MatchableProduction> calculateFirstInSequence(ProductionSequence seq, int startIndex, MappedFirstFollowTable table ,Set<NonTerminal> beingCalculated){

		if (startIndex >= seq.size()){
			return new EmptyPromisseSet<MatchableProduction>();
		}
		Production item = seq.get(startIndex);


		PromisseSet<MatchableProduction> set = calculateFirst(item, table, beingCalculated);

		if (set.contains(EmptyTerminal.instance())) {
			set = set.subtract(EmptyTerminal.instance());

			// because can evaluate to empty, get the first of the next item

			PromisseSet<MatchableProduction> nextset = calculateFirstInSequence(seq, startIndex + 1, table, beingCalculated);
			set = set.union(nextset);

		} 
		return set;
	}

	private PromisseSet<MatchableProduction> calculateFirst(Production p , MappedFirstFollowTable table, Set<NonTerminal> beingCalculated){

		if (p.isTerminal()){
			return table.addFirst(p, p.toTerminal());
		} else if (p.isAutoNonTerminal()){
			return table.addFirst(p, (MatchableProduction)p);
		} else if (p.isSequence()){

			PromisseSet<MatchableProduction> first = calculateFirstInSequence(p.toSequence(), 0, table,beingCalculated);

			table.addFirst(p, first);

			// recursive calculate others
			for (Production d : p.toSequence())
			{
				calculateFirst(d, table,beingCalculated);
			}

			return first;

		} else if (p.isAlternative()){
			RealizedPromisseSet<MatchableProduction> result = new RealizedPromisseSet<MatchableProduction> ();
			for (Production d : p.toAlternative())
			{
				PromisseSet<MatchableProduction> res = calculateFirst(d , table,beingCalculated);
				if (res.isRealised()){
					result = result.union(res);
				}

			}

			table.addFirst(p, result.simplify());
			return result;
		} else if (p.isNonTerminal()){

			PromisseSet<MatchableProduction> firstSet = table.firstOf(p);
			if (!firstSet.isRealised()){
				if (beingCalculated.add(p.toNonTerminal())){
					firstSet = calculateFirst(p.toNonTerminal().getRule(), table,beingCalculated);
					beingCalculated.remove(p.toNonTerminal());
				}

				table.addFirst(p, firstSet);
				firstSet = table.firstOf(p);
			}
			return firstSet;
		} else {
			throw new RuntimeException();
		}

	}



	//	/**
	//	 * @param nonTerminal
	//	 * @param rule
	//	 * @param table
	//	 */
	//	private PromisseSet<MatchableProduction> calculateNonTerminalFirst(NonTerminal n, Production rule, MappedFirstFollowTable table) {
	//
	//
	//		if (rule.isSequence()){
	//			ProductionSequence seq = rule.toSequence();
	//
	//			if (seq.get(0) != n ){
	//				PromisseSet<MatchableProduction> set = calculateFirst(seq, table);
	//				table.addFirst(n, set);
	//			} else {
	//				for (int i = 1 ; i< seq.size(); i++){
	//					calculateFirst(seq.get(i), table);
	//				}
	//			}
	//
	//		} else if (rule.isAlternative()){
	//			for (Production p : rule.toAlternative()){
	//				PromisseSet<MatchableProduction> set = calculateNonTerminalFirst(n, p, table);
	//				table.addFirst(n, set);
	//			}
	//		} else if (rule.isTerminal()) {
	//			PromisseSet<MatchableProduction> set = calculateFirst(rule, table);
	//			table.addFirst(n, set);
	//		} else {
	//			PromisseSet<MatchableProduction> set = calculateNonTerminalFirst(rule.toNonTerminal(), rule.toNonTerminal().getRule(), table);
	//			table.addFirst(n, set);
	//		}
	//
	//		return table.firstOf(n);
	//
	//	}


}
