/**
 * 
 */
package compiler.sense.ast;

import compiler.sense.typesystem.SenseTypeSystem;
import compiler.typesystem.TypeDefinition;


/**
 * 
 */
public class NullValue extends LiteralExpressionNode {

	public TypeDefinition getTypeDefinition() {
		return SenseTypeSystem.None();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLiteralValue() {
		return "null";
	}
}
