/**
 * 
 */
package compiler.sense.typesystem;

import compiler.typesystem.TypeDefinition;
import compiler.typesystem.Variance;

/**
 * 
 */
public interface GenericTypeParameter {

	/**
	 * @return
	 */
	TypeDefinition getLowerBound();
	/**
	 * @return
	 */
	TypeDefinition getUpperbound();
	
	/**
	 * @return
	 */
	Variance getVariance();

	/**
	 * The generic type parameter name, like T or S.
	 * @return
	 */
	String getName();




}
