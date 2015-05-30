package compiler.parser;

import java.util.Optional;

import compiler.lexer.Token;
import compiler.syntax.AstNode;

public class TokenStackItem extends SemanticStackItem  {

	private Token token;

	public TokenStackItem(Token token) {
		this.token = token;
		
		Optional<String> text = token.getText();
		if (text.isPresent()){
			this.setSemanticAttribute("lexicalValue", text.get());
		}
		
	}
	
	public Token getToken(){
		return token;
	}
	
	public String toString(){
		return token.toString();
	}

	@Override
	public String getLabel() {
		return token.getText().orElse("");
	}



}
