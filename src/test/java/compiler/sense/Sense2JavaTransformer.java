/**
 * 
 */
package compiler.sense;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import compiler.java.ast.ArithmeticOperation;
import compiler.java.ast.BlockNode;
import compiler.java.ast.ComparisonNode;
import compiler.java.ast.ExpressionNode;
import compiler.java.ast.ForNode;
import compiler.java.ast.PosExpression;
import compiler.java.ast.VariableDeclarationNode;
import compiler.java.ast.VariableReadNode;
import compiler.parser.IdentifierNode;
import compiler.sense.ast.FieldOrPropertyAccessNode;
import compiler.sense.ast.ForEachNode;
import compiler.sense.ast.NumericValue;
import compiler.sense.ast.RangeNode;
import compiler.sense.ast.SenseAstNode;
import compiler.syntax.AstNode;
import compiler.trees.TreeTransverser;

/**
 * 
 */
public class Sense2JavaTransformer implements Function<AstNode, AstNode> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AstNode apply(AstNode snode) {
		
		if (snode == null){
			return null;
		}
		
		if (snode instanceof IdentifierNode){
			return snode;
		} else if (snode instanceof NumericValue){
			
			return new compiler.java.ast.NumericValue(((NumericValue)snode).getValue());
		} else if (snode instanceof FieldOrPropertyAccessNode){
			FieldOrPropertyAccessNode p = (FieldOrPropertyAccessNode)snode;
			compiler.java.ast.FieldAccessNode n = new compiler.java.ast.FieldAccessNode();
			
			n.setName(p.getName());
			n.setTypeDefinition(n.getTypeDefinition());
			n.setPrimary(apply(p.getPrimary()));
			return n;
		} else if (snode instanceof ForEachNode){
		
			ForEachNode forloop = (ForEachNode)snode;
			if (forloop.getContainer() instanceof RangeNode){
				RangeNode range = (RangeNode) forloop.getContainer();
				ForNode jloop = new ForNode();
				
				jloop.setBlock( (BlockNode) apply(forloop.getBlock()));
				final VariableDeclarationNode init = (VariableDeclarationNode) apply(forloop.getVariableDeclarationNode());
				init.setInitializer((ExpressionNode) apply(range.getStart()));
				jloop.setVariableDeclarationNode( init);
				
				ComparisonNode conditional = new ComparisonNode(ComparisonNode.Operation.LessOrEqualTo);
				conditional.add(new VariableReadNode(forloop.getVariableDeclarationNode().getName()));
				conditional.add(apply(range.getEnd()));
				
				jloop.setConditional(conditional);
				
				PosExpression increment = new PosExpression(ArithmeticOperation.Increment);
				increment.add(new VariableReadNode(forloop.getVariableDeclarationNode().getName()));
				
				jloop.setConditional(increment);
				
				return jloop;
			} 
		} else if (snode instanceof RangeNode){
			return null;
		}
		
		
		try {
			
			Class type = Class.forName(snode.getClass().getName().replaceAll("sense", "java"));
			
			Constructor c;
			try{
			    c = type.getConstructor();
			} catch (NoSuchMethodException e){
				c = type.getConstructors()[0];
			}
			
			AstNode jnode = (AstNode) c.newInstance(new Object[c.getParameters().length]) ;

			Map<String,AstNodeProperty> mapping = new HashMap<>();
			for(AstNodeProperty g : readProperties(jnode.getClass())){
				mapping.put(g.getName(), g);
			}
			
			for(AstNodeProperty f : readProperties(snode.getClass())){
				if (f.getName().equals("children") || f.getName().equals("parent")){
					continue;
				}
				AstNodeProperty g = mapping.get(f.getName());
				
				if (g != null){
					Object obj = f.get(snode);
					
					if (obj instanceof SenseAstNode){
						obj = TreeTransverser.transform((AstNode)obj, this);
					} else if (obj instanceof Enum){
						Class enumType = Class.forName(obj.getClass().getName().replaceAll("sense", "java"));
						
						obj = Enum.valueOf(enumType, ((Enum) obj).name());
					} 
					try{
						g.set(jnode, obj);
					}catch (Exception e){
						throw e;
					}
				}
			
			}
			
			return jnode;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Set<AstNodeProperty> readProperties(Class type){
		
		Set<AstNodeProperty> f = new HashSet<AstNodeProperty>();
		
		collectProperties(type, f);
		return f;
	}

	private static void collectProperties(Class type, Set<AstNodeProperty> fields){
		
		Stream.concat(Stream.of(type.getMethods()), Stream.of(type.getDeclaredMethods()))
		.filter( m ->m.getName().startsWith("set") && !Modifier.isStatic(m.getModifiers()) && m.getParameterCount() == 1)
		.map(m -> {
			
			String name = m.getName().substring(3);
			String prefix =  Boolean.class.isAssignableFrom(m.getReturnType()) ||  Boolean.TYPE.isAssignableFrom(m.getReturnType()) ? "is" : "get";
			Method acessor;
			try {
				acessor = m.getDeclaringClass().getMethod(prefix + name, new Class[0]);
				return  new AstNodeProperty( name.substring(0,1).toLowerCase() + name.substring(1), acessor, m);
			} catch (Exception e) {
				return null;
			}
		}).filter( p -> p != null).collect(Collectors.toCollection(() -> fields));
		
		if (type.getSuperclass() != null){
			collectProperties(type.getSuperclass(), fields);
		}
	}
	
	

}
