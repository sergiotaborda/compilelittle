/**
 * 
 */
package compiler.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import compiler.lexer.ListTokenStream;
import compiler.lexer.ScanPosition;
import compiler.lexer.TokenStream;
import compiler.lexer.Token;

/**
 * 
 */
public class TestTokenStream {

	@Test
	public void test() {
		
		List<Token> tokens = new ArrayList<>();
		
		for (int i = 0 ; i < 20; i++){
			tokens.add(new TestToken(Character.toString((char)('A' + i))));
		}

		TokenStream s = new ListTokenStream(tokens);
		
		assertEquals(new TestToken("A") ,  s.next());
		
		TokenStream d = s.duplicate();
		
		assertEquals(new TestToken("B") ,  d.next());
		assertEquals(new TestToken("C") ,  d.next());
		assertEquals(new TestToken("D") ,  d.next());
		
		assertEquals(new TestToken("B") ,  s.next());
		
		TokenStream f = d.duplicate();
		
		assertEquals(new TestToken("E") ,  f.next());
		assertEquals(new TestToken("F") ,  f.next());
		
		TokenStream g = d.duplicate();
		
		assertEquals(new TestToken("E") ,  g.next());
		assertEquals(new TestToken("F") ,  g.next());
		
		TokenStream h = s.duplicate();
		
		assertEquals(new TestToken("C") ,  h.next());
		assertEquals(new TestToken("D") ,  h.next());
		
	}

	
	
	private static class TestToken implements Token {

		
		private String text;

		public TestToken (String text){
			this.text = text;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isKeyword() {
			return false;
		}
		public int hashCode(){
			 return 0;
		}
		
		public boolean equals(Object other) {
			 return ((TestToken)other).text.equals(text);
		}
		
		public String toString(){
			return text;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public ScanPosition getPosition() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStartLineComment() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStartMultiLineComment() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isId() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEndMultiLineComment() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEndOfFile() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEndOfLine() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStringLiteralStart() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isStringLiteral() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isNumberLiteral() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isOperator() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean match(String text) {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Optional<String> getText() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isWholeNumber() {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isDecimalNumber() {
			throw new UnsupportedOperationException("Not implememented yet");
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isVersionLiteral() {
			throw new UnsupportedOperationException("Not implememented yet");
		}
		
	}
}
