/**
 * 
 */
package compiler.lexer;

import compiler.CompilationUnit;

/**
 * 
 */
public class ScanPosition {

	int lineNumber = 1;
	int columnNumber = 0;
	private CompilationUnit unit;
	
	public ScanPosition(CompilationUnit unit) {
		this.unit = unit;
	}

	/**
	 * Constructor.
	 * @param position
	 */
	public ScanPosition(ScanPosition other) {
		this.lineNumber = other.lineNumber;
		this.columnNumber = other.columnNumber;
		this.unit = other.unit;
	}
	
	public CompilationUnit getCompilationUnit(){
		return unit;
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
