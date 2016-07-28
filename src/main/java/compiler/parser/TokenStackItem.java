package compiler.parser;

import java.util.Optional;

import compiler.lexer.Token;

public class TokenStackItem extends SemanticStackItem  {

	private Token token;

	public TokenStackItem(Token token) {
		this.token = token;
		this.setScanPosition(token.getPosition());
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

		
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof TokenStackItem) && equalsTokenStackItem((TokenStackItem)obj); 
	}
	
	
	private boolean equalsTokenStackItem(TokenStackItem other) {
		return this.token.equals(other.token);
	}
	
	public int hashCode(){
		return token.hashCode();
	}


}


