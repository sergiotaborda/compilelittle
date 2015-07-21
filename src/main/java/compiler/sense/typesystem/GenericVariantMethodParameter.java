/**
 * 
 */
package compiler.sense.typesystem;

import java.util.Optional;

import compiler.sense.typesystem.TypeParameter.Variance;
import compiler.typesystem.Type;

/**
 * 
 */
public class GenericVariantMethodParameter implements MethodParameter {

	
	private String name;
	private int parameterIndex;
	private Type declaringType;

	public GenericVariantMethodParameter(String name, Type declaringType, int parameterIndex){
		this.name = name;
		this.declaringType = declaringType;
		this.parameterIndex = parameterIndex;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type getType() {
		 TypeParameter param = declaringType.getParameters().get(parameterIndex);
		 
		 if (param.getVariance() == Variance.ContraVariant){
			 return Optional.ofNullable(param.getLowerBound()).orElse(SenseType.Nothing);
		 } else {
			 return Optional.ofNullable(param.getUpperbound()).orElse(SenseType.Nothing);
		 }
	}

}
