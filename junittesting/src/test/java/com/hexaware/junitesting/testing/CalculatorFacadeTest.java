package com.hexaware.junitesting.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.hexaware.junittesting.CalculatorFacade;

@DisplayName("Calculator Facade Testing Scenario")
public class CalculatorFacadeTest {
	
	CalculatorFacade calculatorFacade;
	// we need fresh object for every test execution
	@BeforeEach // will be executed before every test case.
	
	void setUp() {
	
		calculatorFacade = new CalculatorFacade(new MathContext(20));
	}
	
	@Nested
	class StatefulOps{
		@Test
		void addMulUndoFlow() {
			calculatorFacade.add(new BigDecimal("10"));
			calculatorFacade.mul(new BigDecimal("3"));
			assertThat(calculatorFacade.getCurrent()).isEqualByComparingTo("30");
			calculatorFacade.undo(); // back to 10
			assertThat(calculatorFacade.getCurrent()).isEqualByComparingTo("10");
		}
	}
	
	@ParameterizedTest(name = "{0} = {1}")
	@CsvSource({"1+2*3,7", "(1+2)*3,9","10/4, 2.5","3+4*2/(1-5), 1"})
	void parameterizedExpressions(String expr, String expected) {
		var out = assertTimeoutPreemptively(Duration.ofMillis(200),
	            () -> calculatorFacade.eval(expr));
		assertThat(out).isEqualByComparingTo
		(new BigDecimal(expected.trim()));
	}

}
