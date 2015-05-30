package compiler;

import java.util.HashMap;
import java.util.Map;

import compiler.parser.MatchableProduction;
import compiler.parser.Production;

public class MappedFirstFollowTable implements FirstFollowTable{
	
	private Map<Production , RealizedPromisseSet<MatchableProduction>> firsts = new HashMap<Production , RealizedPromisseSet<MatchableProduction>>();
	private Map<Production , RealizedPromisseSet<MatchableProduction>> follows = new HashMap<Production , RealizedPromisseSet<MatchableProduction>>();

	public PromisseSet<MatchableProduction> addFirst(Production left, MatchableProduction first) {
		RealizedPromisseSet<MatchableProduction> set = firsts.get(left);
		
		if (set == null){
			set = new RealizedPromisseSet<MatchableProduction>();
			firsts.put(left, set); 
		} 
		
		set.add(first);
		
		return set;
	}
	
	public PromisseSet<MatchableProduction> addFirst(Production left, PromisseSet<MatchableProduction> other) {
		RealizedPromisseSet<MatchableProduction> set = firsts.get(left);
		if (set == null){
			if (other.isRealised()){
			
				set = new RealizedPromisseSet<MatchableProduction>();
				firsts.put(left, set); 
			

				for(MatchableProduction m : other){
					set.add(m);
				}
			} else {
				return new FuturePromisseSet<MatchableProduction>();
			}
		} else {
			if (other.isRealised()){
				for(MatchableProduction m : other){
					set.add(m);
				}
			}
		}

		
		return set;
	}

	@Override
	public PromisseSet<MatchableProduction> firstOf(Production left) {
		RealizedPromisseSet<MatchableProduction> set =  firsts.get(left);
		if (set == null){
			return new FuturePromisseSet<MatchableProduction>();
		}
		return set;
	}

	public  PromisseSet<MatchableProduction>  addFollow(Production left, PromisseSet<MatchableProduction> other) {
		RealizedPromisseSet<MatchableProduction> set = follows.get(left);
		
		if (set == null){
			if (other.isRealised()){
			
				set = new RealizedPromisseSet<MatchableProduction>();
				follows.put(left, set); 
			

				for(MatchableProduction m : other){
					set.add(m);
				}
			} else {
				return new FuturePromisseSet<MatchableProduction>();
			}
		} else {
			if (other.isRealised()){
				for(MatchableProduction m : other){
					set.add(m);
				}
			}
		}
		
		return set;
	}
	
	public  PromisseSet<MatchableProduction>  addFollow(Production left, MatchableProduction follow) {
		RealizedPromisseSet<MatchableProduction> set = follows.get(left);
		
		if (set == null){
			set = new RealizedPromisseSet<MatchableProduction>();
			follows.put(left, set); 
		} 
		
		set.add(follow);
		
		return set;
	}

	public RealizedPromisseSet<MatchableProduction> followOf(Production left) {
		RealizedPromisseSet<MatchableProduction> set =  follows.get(left);
		if (set == null){
			return new RealizedPromisseSet<MatchableProduction>();
		}
		return set;
	}

}
