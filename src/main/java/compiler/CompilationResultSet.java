/**
 * 
 */
package compiler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 */
public class CompilationResultSet {

	private Stream<CompilationResult> stream;

	public CompilationResultSet(Stream<CompilationResult> stream){
		this.stream = stream;
	}
	
	public CompilationResultSet(CompilationResult result){
		this.stream = Stream.of(result);
	}
	
	public Stream<CompilationResult> stream(){
		return stream;
	}
	
	public CompilationResultSet peek(CompilerBackEnd acttion){
		return new CompilationResultSet(stream.peek(node ->{
			if (!node.isError()){
				acttion.use(node.getCompiledUnit());
			}
		}));
	}
	
	public CompilationResultSet passBy(CompilerPhase phase){	
		return new CompilationResultSet(stream.map(node -> phase.apply(node)));
	}
	
	public void sendTo(CompilerBackEnd end){
		stream.forEach(node -> {
			if (node.isError())
			{
				throw node.getThrowable(); 
			} else {
				end.use(node.getCompiledUnit());
			}
		});
	}
	
	public List<CompiledUnit> sendToList(){
		 return stream.map(node -> {
				if (node.isError())
				{
					throw node.getThrowable(); 
				} else {
					return node.getCompiledUnit();
				}
			}).collect(Collectors.toList());
	}
}
