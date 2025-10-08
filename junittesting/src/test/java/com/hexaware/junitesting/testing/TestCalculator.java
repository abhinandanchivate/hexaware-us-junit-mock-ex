package com.hexaware.junitesting.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hexaware.junittesting.Calculator;

public class TestCalculator {
	
	private final Calculator calculator = new Calculator();
	
	@Test
	void testAdd() {
		assertEquals(50, calculator.add(2, 3));
	}
	@Test
	void testSub() {
		assertEquals(5, calculator.sub(10, 5));
	}
	
	@Test
	
	void testMulti() {
		assertEquals(10, calculator.multiply(2, 5));
	}
	@Test
	
	void testDivide() {
		// rt values
		assertEquals(2, calculator.divide(10, 5));
		// zero for b
		assertThrows(IllegalArgumentException.class, ()->calculator.divide(10, 0));
	}

}
