/**
 * 
 */
package compiler.lexer;

/**
 * 
 */
public class ScanPosition {

	int lineNumber = 1;
	int columnNumber = 0;
	
	public ScanPosition() {
		
	}

	/**
	 * Constructor.
	 * @param position
	 */
	public ScanPosition(ScanPosition other) {
		this.lineNumber = other.lineNumber;
		this.columnNumber = other.columnNumber;
	}
	
	/**
	 * Obtains {@link int}.
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	/**
	 * Atributes {@link int}.
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * Obtains {@link int}.
	 * @return the columnNumber
	 */
	public int getColumnNumber() {
		return columnNumber;
	}
	/**
	 * Atributes {@link int}.
	 * @param columnNumber the columnNumber to set
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}
	/**
	 * 
	 */
	public void incrementColumn() {
		this.columnNumber++;
	}
	/**
	 * 
	 */
	public void incrementLine() {
		this.columnNumber = 0;
		this.lineNumber++;
	}
	
	
}
