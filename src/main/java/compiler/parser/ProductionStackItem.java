package compiler.parser;

import java.util.Optional;

import compiler.parser.nodes.ParserTreeNode;




public class ProductionStackItem extends SemanticStackItem  {

	private Production production;

	public ProductionStackItem(Production production) {
		this.production = production;
	}
	
	public Production getProduction(){
		return production;
	}

	public String toString(){
		return production.getLabel();
	}
	
	@Override
	public String getLabel() {
		return production.getLabel();
	}

}
