/**
 * 
 */
package compiler.sense;

public enum ArithmeticOperation {

	    Multiplication ("multiply"),
		Addition("plus"),
		Subtraction("minus"),
		Division("divide"),
		Remainder("remainder"),
		FractionDivision("rationalDivide"),
		RightShift("rightShift"),
		SignedRightShift("signedRightShift"),
		LeftShift("leftShift"),
		Increment("increment"),
		Decrement("decrement");
		
		private String equivalentMethod;
		ArithmeticOperation(String equivalentMethod){
			this.equivalentMethod = equivalentMethod;
		}
		
		public String equivalentMethod(){
			return equivalentMethod;
		}
}