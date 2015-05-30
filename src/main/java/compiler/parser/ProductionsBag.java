package compiler.parser;

import java.util.ArrayList;
import java.util.List;

public class ProductionsBag {

	List<Production> all = new ArrayList<>();
	
	public Production add(Production p){
		
		int pos = all.indexOf(p);
		if (pos < 0){
			all.add(p);
			return p;
		}
		return all.get(pos);
			
	}
	
	public int indexOf(Production p)
	{
		return all.indexOf(p);
	}
	
	public Production getAt(int index)
	{
		return all.get(index);
	}
}
