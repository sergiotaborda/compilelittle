package compiler;

import java.util.LinkedList;
import java.util.List;

public class ComposedCompilerBackEnd implements CompilerBackEnd{

	
	private List<CompilerBackEnd> list = new LinkedList<>();

	public ComposedCompilerBackEnd() {}
	
	public ComposedCompilerBackEnd add(CompilerBackEnd other) {
		this.list.add(other);
		return this;
	}
	
	@Override
    public void beforeAll() {
    	for(var it : list) {
    		it.beforeAll();
    	}
    };
    
    @Override
    public void afterAll() {
    	for(var it : list) {
    		it.afterAll();
    	}
    };
	
	@Override
	public void use(CompiledUnit unit) {
		for(var it : list) {
    		it.use(unit);
    	}
	}

}
