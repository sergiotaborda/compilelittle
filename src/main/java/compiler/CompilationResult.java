/**
 * 
 */
package compiler;

import java.util.function.Function;

/**
 * 
 */
public class CompilationResult {

	private CompiledUnit success;
	private RuntimeException error;
	private boolean isError = false;

	public CompilationResult(CompiledUnit success){
		if (success == null){
			throw new IllegalArgumentException("Result is required");
		}
		this.success = success; 
	}
	
	public CompilationResult(RuntimeException error){
		this.error = error;
		isError = true;
	}
	
	public CompiledUnit getCompiledUnit(){
		return success;
	}
	
	public RuntimeException getThrowable(){
		return error;
	}
	
	public  CompilationResult map(Function<CompiledUnit, CompiledUnit> mapper) {
		if (isError){
			return new CompilationResult(error);
		} else {
			return new CompilationResult(mapper.apply(success));
		}
	}

	/**
	 * @return
	 */
	public boolean isError() {
		return isError;
	}
}
