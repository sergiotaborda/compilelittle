package compiler.reference1;

import java.util.ArrayList;
import java.util.List;

public class Node   {

	
	private String lexical;
	private List<Node> nodes = new ArrayList<>();
	
	public Node(String lexical){
		this.lexical = lexical;
	}
	public Node(){
		
	}

	public void add(Node node){
		nodes.add(node);
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		
		print(this, builder);

	
		return builder.toString();
	}
	
	private void print(Node node, StringBuilder builder) {
		
		if (node.nodes.isEmpty()){
			builder.append(node.lexical);
		} else {
			for(Node n : node.nodes){
				print(n, builder);
			}
		}
	}
}
