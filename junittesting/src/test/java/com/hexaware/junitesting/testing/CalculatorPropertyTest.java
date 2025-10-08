package com.hexaware.junitesting.testing;

import java.math.BigDecimal;
import java.math.MathContext;

import com.hexaware.junittesting.CalculatorCore;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class CalculatorPropertyTest {

	// @Property : mark a method as a property test.
	// @Example : marks a method as a simple example based test
	// Arbitraries : a factory for generating arbitratory data(string, numbers, list, etc).
	/// generate our testing data 
	// @provide : marks a method that provides custom data generator
	// @forall : annotation for method parameters to inject generated values.
	// @combinators : to combine multiple generators.
	// a+b = b+a
	
	@Property
	boolean additionIsCommutative(@ForAll("decimals") BigDecimal a, 
			@ForAll("decimals") BigDecimal b) {
		var mc = new MathContext(20);
		var core = new CalculatorCore(mc);
		var left = core.add(a, b); // a+b
		var right = core.add(b, a); // b+a 
		return left.compareTo(right)==0; // results are same.
		
	}
	
	@Provide //==> this will generate / provide the values.
		Arbitrary<BigDecimal>	decimals(){
		System.out.println("hello");
		// -1000 to 1000
		return Arbitraries.integers().between(-1_000, 1_000).flatMap(i->Arbitraries.integers().between(0, 4)
				.map(scale->new BigDecimal(i).movePointLeft(scale)));
		// i  = 678 scale =0 ==> 678
		// i  = 678 scale =2 ==> 6.78
		
	}
	
}
