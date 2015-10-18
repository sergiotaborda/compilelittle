package compiler;

import static java.lang.Math.pow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// TODO transform to JUnit
public class Test1 implements Runnable {

	
	public static void main(String[] args) {
		try {
			int a = 2;
			
			a++;
			
			Integer b = 3;
			
			int c = a + b + 5;
			
			
			if (!(c == 10)){
				System.out.println("Correct");
			} else {
				System.out.println("Incorrect result");
			}
			
			if(c!=10){System.out.println("Now".length());}
			
			double d = 3.14;
			
			a++;
			
			Number n = c + d;
			System.out.println(n);
			
			List<Integer> list = new ArrayList<>();
			do{
				d = d + 5;
				d++;
				list.add((int)pow(a, 2));
				
				--a;
			}while (a >=0);
			
			for (Integer i : list){
				switch (i){
				case 1:
					System.out.println("1".length());
					break;
				case 2:
					System.out.println("2".length());
				default:
					
				}
			}
			Function<Integer, Integer> func = Math::abs;
			list.stream().map( it -> func);
			
			
		} catch (Exception e){
			
		} finally {
			
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		throw new UnsupportedOperationException("Not implememented yet");
	}
}
