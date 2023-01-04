/**
 * 
 */
package compiler;

import compiler.lexer.ScanPosition;
import compiler.lexer.ScanPositionHolder;

/**
 * 
 */
public class CompilationError extends RuntimeException {

	private static final long serialVersionUID = 5239871682414732773L;
	
	private final ScanPositionHolder holder;
	
	public CompilationError(String msg) {
		super(msg);
		this.holder = null;
	}

	public CompilationError(ScanPosition position, String msg) {
		super(msg(position, msg));
		this.holder = position == null ? null :  new ScanPositionHolder() {

			@Override
			public ScanPosition getScanPosition() {
				return position;
			}

			@Override
			public void setScanPosition(ScanPosition position) {
				//no-op
			}
			
		};
	}

	private static String msg(ScanPosition position, String msg) {
		if (position == null) {
			return msg;
		}
		return msg + " at " + position;
	}

	public CompilationError(ScanPositionHolder holder, String msg) {
		this(holder == null ? null : holder.getScanPosition(), msg);
	}

	public ScanPositionHolder getScanPositionHolder() {
		return holder;
	}
}
